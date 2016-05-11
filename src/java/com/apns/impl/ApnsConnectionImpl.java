package com.apns.impl;

import static com.apns.model.ApnsConstants.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.SocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apns.IApnsConnection;
import com.apns.model.Command;
import com.apns.model.ErrorResponse;
import com.apns.model.Payload;
import com.apns.model.PushNotification;
import com.apns.tools.ApnsTools;

public class ApnsConnectionImpl implements IApnsConnection {
	
	private static AtomicInteger IDENTIFIER = new AtomicInteger(100);
	
	private Logger logger = LoggerFactory.getLogger(ApnsConnectionImpl.class);
	
	private int EXPIRE = Integer.MAX_VALUE;
	
	private SocketFactory factory;

	private Socket socket;

	private Queue<PushNotification> notificationCachedQueue = new LinkedList<PushNotification>();

	private boolean errorHappendedLastConn = false;
	
	private boolean isFirstWrite = false;
	
	private int maxRetries;
	private int maxCacheLength;
	
	private int readTimeOut;
	
	private String host;
	private int port;
	
	private String name;
	
	private String connName;
	private int intervalTime;
	private long lastSuccessfulTime = 0;
	
	private AtomicInteger notificaionSentCount = new AtomicInteger(0);
	
	private Object lock = new Object();
	
	public ApnsConnectionImpl(SocketFactory factory, String host, int port, int maxRetries, 
			int maxCacheLength, String name, String connName, int intervalTime, int timeout) {
		this.factory = factory;
		this.host = host;
		this.port = port;
		this.maxRetries = maxRetries;
		this.maxCacheLength = maxCacheLength;
		this.name = name;
		this.connName = connName;
		this.intervalTime = intervalTime;
		this.readTimeOut = timeout;
	}
	
	@Override
	public void sendNotification(String token, Payload payload) {
		PushNotification notification = new PushNotification();
		notification.setId(IDENTIFIER.incrementAndGet());
		notification.setExpire(EXPIRE);
		notification.setToken(token);
		notification.setPayload(payload);
		sendNotification(notification);
	}
	
	@Override
	public void sendNotification(PushNotification notification) {
		byte[] plBytes = null;
		String payload = notification.getPayload().toString();
		try {
			plBytes = payload.getBytes(CHARSET_ENCODING);
			if (plBytes.length > PAY_LOAD_MAX_LENGTH) {
				logger.error("自动截取前@推送消息长度超过"+PAY_LOAD_MAX_LENGTH+"个字符:" + payload);
				String alert=notification.getPayload().getAlert();
				int pct=PAY_LOAD_MAX_LENGTH-(plBytes.length-alert.getBytes(CHARSET_ENCODING).length)/("中".getBytes(CHARSET_ENCODING).length);
				notification.getPayload().setAlert(alert.substring(0,pct));
				logger.error("自动截取后@推送消息长度超过"+PAY_LOAD_MAX_LENGTH+"个字符:" + payload);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("@sunshine:"+e.getMessage(), e);
			return;
		}
		
		/**
         *如果发现当前连接有error-response，加锁等待，直到另外一个线程把重发做完后再继续发送  
		 */
		synchronized (lock) {
			if (errorHappendedLastConn) {
				closeSocket(socket);
				socket = null;
			}
			byte[] data = notification.generateData(plBytes);
			boolean isSuccessful = false;
			int retries = 0;
			while (retries < maxRetries) {
				try {
					boolean exceedIntervalTime = lastSuccessfulTime > 0 && (System.currentTimeMillis() - lastSuccessfulTime) > intervalTime;
					if (exceedIntervalTime) {
						closeSocket(socket);
						socket = null;
					}
					
					if (socket == null || socket.isClosed()) {
						socket = createNewSocket();
					}
					
					OutputStream socketOs = socket.getOutputStream();
					socketOs.write(data);
					socketOs.flush();
					isSuccessful = true;
					break;
				} catch (Exception e) {
					logger.error(connName + " " + e.getMessage(), e);
					closeSocket(socket);
					socket = null;
				}
				retries++;
			}
			if (!isSuccessful) {
				logger.error(String.format("%s Notification send failed. %s", connName, notification));
				return;
			} else {
				logger.info(String.format("%s Send success. count: %s, notificaion: %s", connName, 
						notificaionSentCount.incrementAndGet(), notification));
				
				notificationCachedQueue.add(notification);
				lastSuccessfulTime = System.currentTimeMillis();	
				if (notificationCachedQueue.size() > maxCacheLength) {
					notificationCachedQueue.poll();
				}
			}
		}

		if (isFirstWrite) {
			isFirstWrite = false;
			startErrorWorker();
		}
	}
	private Socket createNewSocket() throws IOException, UnknownHostException {
		if (logger.isDebugEnabled()) {
			logger.debug(connName + " create a new socket.");
		}
		isFirstWrite = true;
		errorHappendedLastConn = false;
		Socket socket = factory.createSocket(host, port);
		socket.setSoTimeout(readTimeOut);
		socket.setTcpNoDelay(true);
		
		return socket;
	}
	private void closeSocket(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	private boolean isSocketAlive(Socket socket) {
		if (socket != null && socket.isConnected()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void close() throws IOException {
		closeSocket(socket);
	}
	
	private void startErrorWorker() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Socket curSocket = socket;
				try {
					if (!isSocketAlive(curSocket)) {
						return;
					}
					InputStream socketIs = curSocket.getInputStream();
				    byte[] res = new byte[ERROR_RESPONSE_BYTES_LENGTH];
					int size = 0;
					
					while (true) {
						try {
							size = socketIs.read(res);
							if (size > 0 || size == -1) {
								break;
							}
						} catch (SocketTimeoutException e) {
						    Thread.sleep(10);
						}
					}
					
					int command = res[0];
					/**
					 * 一旦遇到错误返回就关闭连接，并且重新发送在它之后发送的通知
					 */			
					if (size == res.length && command == Command.ERROR) {
						int status = res[1];
						int errorId = ApnsTools.parse4ByteInt(res[2], res[3], res[4], res[5]);
						
						if (logger.isInfoEnabled()) {
							logger.info(String.format("%s Received error response. status: %s, id: %s, error-desc: %s", connName, status, errorId, ErrorResponse.desc(status)));
						}
						
						Queue<PushNotification> resentQueue = new LinkedList<PushNotification>();

						synchronized (lock) {
							boolean found = false;
							errorHappendedLastConn = true;
							while (!notificationCachedQueue.isEmpty()) {
								PushNotification pn = notificationCachedQueue.poll();
								if (pn.getId() == errorId) {
									found = true;
								} else {
									if (found) {
										resentQueue.add(pn);
									}
								}
							}
							if (!found) {
								logger.warn(connName + " Didn't find error-notification in the queue. Maybe it's time to adjust cache length. id: " + errorId);
							}
						}

						if (!resentQueue.isEmpty()) {
							ApnsResender.getInstance().resend(name, resentQueue);
						}
					} else {
						logger.error(connName + " Unexpected command or size. commend: " + command + " , size: " + size);
					}
				} catch (Exception e) {
					logger.error(connName + " " + e.getMessage());
				} finally {
					closeSocket(curSocket);
				}
			}
		});
		
		thread.start();
	}
}