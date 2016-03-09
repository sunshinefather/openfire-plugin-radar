package com.apns.model;

public class FrameItem {
	/* 
	 * Item Id, Item Name, Length
	 * 1·Device token,             32 bytes
	 * 2·Payload,                  <=256 bytes
	 * 3·Notification identifier,  4 bytes
	 * 4·Expiration date,          4 bytes
	 * 5·Priority,                 1 bytes
	 */
	public static final int ITEM_ID_DEVICE_TOKEN = 1;
	public static final int ITEM_ID_PAYLOAD= 2;
	public static final int ITEM_ID_NOTIFICATION_IDENTIFIER = 3;
	public static final int ITEM_ID_EXPIRATION_DATE = 4;
	public static final int ITEM_ID_PRIORITY = 5;
	
	private int itemId;
	private int itemLength;
	private byte[] itemData;
	
	public FrameItem(int itemId, byte[] itemData) {
		this.itemId = itemId;
		this.itemData = itemData;
		this.itemLength = itemData.length;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getItemLength() {
		return itemLength;
	}
	public void setItemLength(int itemLength) {
		this.itemLength = itemLength;
	}
	public byte[] getItemData() {
		return itemData;
	}
	public void setItemData(byte[] itemData) {
		this.itemData = itemData;
	}
}
