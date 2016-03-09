package com.apns.model;

public class ApnsConstants {
	//开发环境 地址和端口
	public static final String HOST_DEVELOPMENT_ENV = "gateway.sandbox.push.apple.com";
	public static final int PORT_DEVELOPMENT_ENV = 2195;
	
	//生产环境 地址和端口
	public static final String HOST_PRODUCTION_ENV = "gateway.push.apple.com";
	public static final int PORT_PRODUCTION_ENV = 2195;
	
	//开发环境 回调地址和端口
	public static final String FEEDBACK_HOST_DEVELOPMENT_ENV = "feedback.sandbox.push.apple.com";
	public static final int FEEDBACK_PORT_DEVELOPMENT_ENV = 2196;
	
	//生产环境 回调地址和端口
	public static final String FEEDBACK_HOST_PRODUCTION_ENV = "feedback.push.apple.com";
	public static final int FEEDBACK_PORT_PRODUCTION_ENV = 2196;
	
	public static final String KEYSTORE_TYPE = "PKCS12";
	public static final String ALGORITHM = "sunx509";
	public static final String PROTOCOL = "TLS";
	
	public static final int ERROR_RESPONSE_BYTES_LENGTH = 6;
	
	public static final int PAY_LOAD_MAX_LENGTH = 512;
	
	public static final String CHARSET_ENCODING = "UTF-8";
}
