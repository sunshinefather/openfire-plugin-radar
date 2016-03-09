package com.apns.model;

import java.util.Date;
public class Feedback {

	private long time;

	private String token;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getDate() {
		return new Date(getTime() * 1000);
	}
}
