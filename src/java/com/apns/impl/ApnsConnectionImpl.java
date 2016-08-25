package com.apns.impl;

import static com.apns.model.ApnsConstants.CHARSET_ENCODING;
import static com.apns.model.ApnsConstants.ERROR_RESPONSE_BYTES_LENGTH;
import static com.apns.model.ApnsConstants.PAY_LOAD_MAX_LENGTH;
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
	
	private static AtomicInteger IDENTIFIER = new AtomicInteger(0);
	
	private Logger logger = LoggerFactory.getLogger(ApnsConnectionImpl.class);
	
	private int EXPIRE = Integer.MAX_VALUE;
	
	private SocketFactory factory;

	private Socket socket;

	private Queue<PushNotification> notificationCachedQueue = new LinkedList<PushNotification>();

	private volatile boolean errorHappendedLastConn = false;
	
	private volatile boolean isFirstWrite = false;
	
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
	
	private Thread thread=null;
	
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
				String alert=notification.getPayload().getAlert();
				int pct=PAY_LOAD_MAX_LENGTH-(plBytes.length-alert.getBytes(CHARSET_ENCODING).length)/("中".getBytes(CHARSET_ENCODING).length);
				notification.getPayload().setAlert(alert.substring(0,pct));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("@sunshine: apns不支持的字符串", e);
			return;
		}
		
		/**
         *如果发现当前连接有error-response，加锁等待，直到另外一个线程把重发做完后再继续发送  
		 */
		synchronized (lock) {
			byte[] data = notification.generateData();
			boolean isSuccessful = false;
			int retries = 0;
			while (retries < maxRetries) {
				boolean isNewCreaded=false;
				try {
					if (errorHappendedLastConn) {
						socket = null;
				     }
					
					boolean exceedIntervalTime = lastSuccessfulTime > 0 && (System.currentTimeMillis() - lastSuccessfulTime) > intervalTime;
					if (exceedIntervalTime) {
						logger.error(connName+" @sunshine:apns推送强制断开,超过30分钟未发送出消息");
						closeSocket(socket);
						socket = null;
					}
					
					if (!isSocketAlive(socket)) {
						logger.error(connName+" @sunshine:apns推送连接已断开");
						closeSocket(socket);
						socket = createNewSocket();
						isNewCreaded=true;
					}
					
					OutputStream socketOs = socket.getOutputStream();
					socketOs.write(data);
					socketOs.flush();
					isSuccessful = true;
					break;
				} catch (Exception e) {
					logger.error(connName+" @sunshine:apns推送异常:第"+(1+retries) + "次 " ,e);
					if(isNewCreaded){
						closeSocket(socket);
					}
					socket = null;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				retries++;
			}
			if (!isSuccessful) {
				logger.error(connName+" @sunshine:apns推送失败. "+notification.getToken());
				return;
			} else {
				logger.info(String.format("%s @sunshine:apns推送成功. count: %s, notificaion: %s", connName,notificaionSentCount.incrementAndGet(), notification));
				notificationCachedQueue.offer(notification);
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
		Socket socket=null;
		if (logger.isDebugEnabled()) {
			logger.debug(connName + " @sunshine:apns创建一个新的连接");
		}
		isFirstWrite = true;
		errorHappendedLastConn = false;
		socket = factory.createSocket(host, port);
		socket.setSoTimeout(readTimeOut);
		socket.setTcpNoDelay(true);
		return socket;
	}
	
	private void closeSocket(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
				socket=null;
			}
		} catch (Exception e) {
			logger.error("@sunshine:关闭连接异常",e);
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
		thread = new Thread(new Runnable() {

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
						}catch (SocketTimeoutException e) {
							Thread.sleep(10);
						}catch (Exception e) {
							logger.error(connName+" @sunshine:apns读取失败",e);
							break;
						}
					}
					
					int command = res[0];
					
					/**
					 * 一旦遇到错误返回就关闭连接，并且重新发送在它之后发送的通知
					 */			
					Queue<PushNotification> resentQueue = new LinkedList<PushNotification>();
					synchronized (lock) {
					errorHappendedLastConn = true;
					if (size == res.length && (command == Command.INVALID_TOKEN || command == Command.SHUTDOWN)) {
						int status = res[1];
						int errorId = ApnsTools.parse4ByteInt(res[2], res[3], res[4], res[5]);
						if (logger.isInfoEnabled()) {
							logger.error(String.format("%s @sunshine:收到apns服务器响应. status: %s, id: %s, error-desc: %s", connName, status, errorId, ErrorResponse.desc(status)));
						}
						boolean found = false;
						int notifiqueueSize=notificationCachedQueue.size();
						while (!notificationCachedQueue.isEmpty()) {
								PushNotification pn = notificationCachedQueue.poll();
								if (pn.getId() == errorId) {
									found = true;
								} else {
									if (found) {
										resentQueue.add(pn);
										if(!notificationCachedQueue.isEmpty()){
											resentQueue.addAll(notificationCachedQueue);
											notificationCachedQueue.clear();
										}
									}
								}
							}
						
							if (!found) {
								logger.error(String.format("%s @sunshine:apns异常断开 ,从 %s 条发送数据中未找到失效token数据. id: %s",connName,notifiqueueSize,errorId));
							}
						
						
						
					} else if(command ==0 && size==0) {
						logger.error(String.format("%s @sunshine:apns异常断开 ,当前队列已发数据  %s ", connName,notificationCachedQueue.size()));
					}else{
						logger.error(String.format("%s @sunshine:apns异常断开 ,command: %s  size: %s .", connName,command,size));
					}
				}
	
				if (!resentQueue.isEmpty()) {
					    logger.error(String.format("%s @sunshine:apns重推  %s 条数据",connName,resentQueue.size()));
						ApnsResender.getInstance().resend(name, resentQueue);
					}
				
				} catch (Exception e) {
					logger.error(connName+" @sunshine:apns监听异常:",e);
				} finally {
					closeSocket(curSocket);
				}
			}
		});
		thread.start();
	}
}