package com.apns;

import java.io.Closeable;
import com.apns.model.Payload;
import com.apns.model.PushNotification;

public interface IApnsConnection extends Closeable {
	
	public void sendNotification(String token, Payload payload);

	public void sendNotification(PushNotification notification);
}
