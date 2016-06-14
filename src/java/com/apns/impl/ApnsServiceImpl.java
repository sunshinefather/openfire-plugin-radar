package com.apns.impl;

import static com.apns.model.ApnsConstants.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apns.IApnsConnection;
import com.apns.IApnsFeedbackConnection;
import com.apns.IApnsService;
import com.apns.model.ApnsConfig;
import com.apns.model.Feedback;
import com.apns.model.Payload;
import com.apns.model.PushNotification;
import com.apns.tools.ApnsTools;

public class ApnsServiceImpl implements IApnsService {
	private static Logger logger = LoggerFactory.getLogger(ApnsServiceImpl.class);
	private ExecutorService service = null;
	private ApnsConnectionPool connPool = null;
	private IApnsFeedbackConnection feedbackConn = null;
	private ApnsServiceImpl(ApnsConfig config) {
		int poolSize = config.getPoolSize();
		service = Executors.newFixedThreadPool(poolSize);
		
		SocketFactory factory = ApnsTools.createSocketFactory(config.getKeyStore(), config.getPassword(), 
				KEYSTORE_TYPE, ALGORITHM, PROTOCOL);
		connPool = ApnsConnectionPool.newConnPool(config, factory);
		feedbackConn = new ApnsFeedbackConnectionImpl(config, factory);
	}
	
	@Override
	public void sendNotification(final String token, final Payload payload) {
		service.execute(new Runnable() {
			@Override
			public void run() {
				IApnsConnection conn = null; 
				try {
					conn = getConnection();
					conn.sendNotification(token, payload);
				} catch (Exception e) {
					logger.error("@sunshine:apns推送网络异常", e);
				} finally {
					if (conn != null) {
						connPool.returnConn(conn);
					}
				}
			}
		});
	}
	@Override
	public void sendNotification(final PushNotification notification) {
		service.execute(new Runnable() {
			@Override
			public void run() {
				IApnsConnection conn = null; 
				try {
					conn = getConnection();
					conn.sendNotification(notification);
				} catch (Exception e) {
					logger.error("@sunshine:apns推送网络异常 ", e);
				} finally {
					if (conn != null) {
						connPool.returnConn(conn);
					}
				}
			}
		});
	}
	private IApnsConnection getConnection() {
		IApnsConnection conn = connPool.borrowConn();
		if (conn == null) {
			throw new RuntimeException("获取连接失败");
		}
		return conn;
	}
	
	private static void checkConfig(ApnsConfig config) {
		if (config == null || config.getKeyStore() == null || config.getPassword() == null || 
				"".equals(config.getPassword().trim())) {
			throw new IllegalArgumentException("证书或密码不能为空！");
		}
		if (config.getPoolSize() <= 0 || config.getRetries() <= 0 || config.getCacheLength() <= 0) {
			throw new IllegalArgumentException("apns推送参数配置错误");
		}
	}
	private static Map<String, IApnsService> serviceCacheMap = new HashMap<String, IApnsService>(3);
	
	public static IApnsService getCachedService(String name) {
		return serviceCacheMap.get(name);
	}
	
	public static IApnsService createInstance(ApnsConfig config) {
		checkConfig(config);
		String name = config.getName();
		IApnsService service = getCachedService(name);
		if (service == null) {
			synchronized (name.intern()) {
				service = getCachedService(name);
				if (service == null) {
					service = new ApnsServiceImpl(config);
					serviceCacheMap.put(name, service);
				}
			}
		}
		return service;
	}

	@Override
	public void shutdown() {
		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("@sunshine:apns服务器关闭", e);
        }
        connPool.close();
	}

	@Override
	public List<Feedback> getFeedbacks() {
		return feedbackConn.getFeedbacks();
	}
	
	@Override
	public int remainConnPoolSize() {
      return connPool.currentConnnectionPoolSize();
	}
}