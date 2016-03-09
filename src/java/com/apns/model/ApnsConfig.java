package com.apns.model;

import java.io.InputStream;
public class ApnsConfig {
	private String name;
	
	/**
	 * 证书文件输入流
	 */
	private InputStream keyStore;
	
	/**
	 * 证书密码
	 */
	private String password;
	
	/**
	 * true:  开发环境
	 * false: 生产环境 
	 */
	private boolean isDevEnv = false;
	
	/**
	 *连接池
	 */
	private int poolSize = 5;
    
	/**
	 * 发送失败堆积池
	 */
	private int cacheLength = 100;
	/**
	 *发送错误重发次数少
	 */
	private int retries = 3;
	
	private int intervalTime = 30 * 60 * 1000; // 30 minutes
	
	// socket 超时时间
	private int timeout = 10 * 1000; // 10 seconds
	
	public InputStream getKeyStore() {
		return keyStore;
	}
	
	public void setKeyStore(String keystore) {
		InputStream is = ApnsConfig.class.getClassLoader().getResourceAsStream(keystore);
		if (is == null) {
			throw new IllegalArgumentException("Keystore file not found. " + keystore);
		}
		setKeyStore(is);
	}
	
	public void setKeyStore(InputStream keyStore) {
		this.keyStore = keyStore;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isDevEnv() {
		return isDevEnv;
	}
	public void setDevEnv(boolean isDevEnv) {
		this.isDevEnv = isDevEnv;
	}
	public int getPoolSize() {
		return poolSize;
	}
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	public int getCacheLength() {
		return cacheLength;
	}
	public void setCacheLength(int cacheLength) {
		this.cacheLength = cacheLength;
	}
	public int getRetries() {
		return retries;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	public String getName() {
		if (name == null || "".equals(name.trim())) {
			if (isDevEnv()) {
				return "dev-env";
			} else {
				return "product-env";
			}
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}