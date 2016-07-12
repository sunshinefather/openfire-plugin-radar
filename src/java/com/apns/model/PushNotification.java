package com.apns.model;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.apns.tools.ApnsTools;

public class PushNotification {

	public static final int PRIORITY_SENT_IMMEDIATELY = 10;
	public static final int PRIORITY_SENT_A_TIME = 5;
	private static final Pattern pattern = Pattern.compile("[ -]");
	
	private int id;
	private int expire;
	private String token;
	private Payload payload;
	private int priority = PRIORITY_SENT_IMMEDIATELY;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = pattern.matcher(token).replaceAll("");
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public byte[] generateData() {
		byte[] tokens = ApnsTools.decodeHex(getToken());
		List<FrameItem> list = new LinkedList<FrameItem>();
		list.add(new FrameItem(FrameItem.ITEM_ID_DEVICE_TOKEN, tokens));
		try {
			list.add(new FrameItem(FrameItem.ITEM_ID_PAYLOAD,getPayload().toString().getBytes(ApnsConstants.CHARSET_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		list.add(new FrameItem(FrameItem.ITEM_ID_NOTIFICATION_IDENTIFIER, ApnsTools.intToBytes(getId(), 4)));
		list.add(new FrameItem(FrameItem.ITEM_ID_EXPIRATION_DATE, ApnsTools.intToBytes(getExpire(), 4)));
		list.add(new FrameItem(FrameItem.ITEM_ID_PRIORITY, ApnsTools.intToBytes(getPriority(), 1)));
		return ApnsTools.generateData(list);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id="); sb.append(getId());
		sb.append(" token="); sb.append(getToken());
		sb.append(" payload="); sb.append(getPayload().toString());
		return sb.toString();
	}
	public int getPriority() {
		return priority;
	}
}