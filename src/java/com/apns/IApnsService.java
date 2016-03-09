package com.apns;

import java.util.List;

import com.apns.model.Feedback;
import com.apns.model.Payload;
import com.apns.model.PushNotification;

public interface IApnsService {
	/**
	 * @param token  deviceToken
	 * @param payload
	 */
	public void sendNotification(String token, Payload payload);
	/**
	 * If you want to specify the ID of a notification, use this method
	 * @param notification
	 */
	public void sendNotification(PushNotification notification);
	
	public void shutdown();
	
	/**
     *返回用户在设备上卸载了APP的device token。这个接口最好定期调用，比如一天一次，或者一小时一次等等
	 */
	public List<Feedback> getFeedbacks();
}
