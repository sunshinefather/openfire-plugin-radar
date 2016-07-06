package com.apns.impl;

import static com.apns.model.ApnsConstants.HOST_DEVELOPMENT_ENV;
import static com.apns.model.ApnsConstants.HOST_PRODUCTION_ENV;
import static com.apns.model.ApnsConstants.PORT_DEVELOPMENT_ENV;
import static com.apns.model.ApnsConstants.PORT_PRODUCTION_ENV;
import java.io.Closeable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.net.SocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apns.IApnsConnection;
import com.apns.model.ApnsConfig;
public class ApnsConnectionPool implements Closeable {
	private static int CONN_ID_SEQ = 1;
	private SocketFactory factory;
	private BlockingQueue<IApnsConnection> connQueue = null;
	private static final Logger log = LoggerFactory.getLogger(ApnsConnectionPool.class);
	private ApnsConnectionPool(ApnsConfig config, SocketFactory factory) {
		this.factory = factory;
		
		String host = HOST_PRODUCTION_ENV;
		int port = PORT_PRODUCTION_ENV;
		if (config.isDevEnv()) {
			host = HOST_DEVELOPMENT_ENV;
			port = PORT_DEVELOPMENT_ENV;
		}
		
		int poolSize = config.getPoolSize();
		connQueue = new LinkedBlockingQueue<IApnsConnection>(poolSize);
		
		for (int i = 0; i < poolSize; i++) {
			String connName = (config.isDevEnv() ? "dev-" : "pro-") + CONN_ID_SEQ++;
			IApnsConnection conn = new ApnsConnectionImpl(this.factory, host, port, config.getRetries(),config.getCacheLength(), config.getName(), connName, config.getIntervalTime(), config.getTimeout());
			connQueue.add(conn);
		}
	}
	
	public IApnsConnection borrowConn() {
		try {
			return connQueue.take();
		} catch (Exception e) {
			log.error("@sunshine:apns 获取连接失败");
		}
		return null;
	}
	
	public void returnConn(IApnsConnection conn) {
		if (conn != null) {
			connQueue.add(conn);
		}
	}
	
	public int currentConnnectionPoolSize() {
		int count=0;
		if(connQueue!=null){
			count=connQueue.size();
		}
      return count;
	}
	
	@Override
	public void close() {
		while (!connQueue.isEmpty()) {
			try {
				connQueue.take().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ApnsConnectionPool newConnPool(ApnsConfig config, SocketFactory factory) {
		return new ApnsConnectionPool(config, factory);
	}
}
