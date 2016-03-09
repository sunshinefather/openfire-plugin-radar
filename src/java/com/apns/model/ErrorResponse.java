package com.apns.model;

public class ErrorResponse {
	public static final int ERROR_CODE_NO_ERRORS = 0;
	public static final int ERROR_CODE_PROCESSING_ERROR = 1;
	public static final int ERROR_CODE_MISSING_TOKEN = 2;
	public static final int ERROR_CODE_MISSING_TOPIC = 3;
	public static final int ERROR_CODE_MISSING_PAYLOAD = 4;
	public static final int ERROR_CODE_INVALID_TOKEN_SIZE = 5;
	public static final int ERROR_CODE_INVALID_TOPIC_SIZE = 6;
	public static final int ERROR_CODE_INVALID_PAYLOAD_SIZE = 7;
	public static final int ERROR_CODE_INVALID_TOKEN = 8;
	public static final int ERROR_CODE_SHUTDOWN = 10;
	public static final int ERROR_CODE_NONE = 255;
	
	public static String desc(int code) {
		String desc = null;
		switch (code) {
		case ERROR_CODE_NO_ERRORS:
			desc = "No errors encountered"; break;
		case ERROR_CODE_PROCESSING_ERROR:
			desc = "Processing error"; break;
		case ERROR_CODE_MISSING_TOKEN:
			desc = "Missing device token"; break;
		case ERROR_CODE_MISSING_TOPIC:
			desc = "Missing topic"; break;
		case ERROR_CODE_MISSING_PAYLOAD:
			desc = "Missing payload"; break;
		case ERROR_CODE_INVALID_TOKEN_SIZE:
			desc = "Invalid token size"; break;
		case ERROR_CODE_INVALID_TOPIC_SIZE:
			desc = "Invalid topic size"; break;
		case ERROR_CODE_INVALID_PAYLOAD_SIZE:
			desc = "Invalid payload size"; break;
		case ERROR_CODE_INVALID_TOKEN:
			desc = "Invalid token"; break;
		case ERROR_CODE_SHUTDOWN:
			desc = "Shutdown"; break;
		default:
			desc = "Unkown"; break;
		}
		return desc;
	}
}