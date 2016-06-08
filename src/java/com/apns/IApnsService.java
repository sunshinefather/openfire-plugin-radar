package com.apns;

import java.util.List;

import com.apns.model.Feedback;
import com.apns.model.Payload;
import com.apns.model.PushNotification;

public interface IApnsService {
    /**
     * 推送通知
     */
	public void sendNotification(String token, Payload payload);
    /**
     * 推送通知(指定id)
     */
	public void sendNotification(PushNotification notification);
	/**
	 * 关闭
	 */
	public void shutdown();
	
	/**
     *返回用户在设备上卸载了APP的device token。这个接口最好定期调用，比如一天一次，或者一小时一次等等
	 */
	public List<Feedback> getFeedbacks();
	/**
	 *当前连接池剩余连接数
	 */
	public int remainConnPoolSize();
}
