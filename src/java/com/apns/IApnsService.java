package com.apns;

import java.util.List;

import com.apns.model.Feedback;
import com.apns.model.Payload;
import com.apns.model.PushNotification;

public interface IApnsService {
    /**
     * 推送通知
     * @Title: sendNotification
     * @Description: TODO  
     * @param: @param token
     * @param: @param payload      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public void sendNotification(String token, Payload payload);
    /**
     * 推送通知(指定id)
     * @Title: sendNotification
     * @Description: TODO  
     * @param: @param notification      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public void sendNotification(PushNotification notification);
	/**
	 * 关闭
	 * @Title: shutdown
	 * @Description: TODO  
	 * @param:       
	 * @return: void
	 * @author: sunshine  
	 * @throws
	 */
	public void shutdown();
	
	/**
     *返回用户在设备上卸载了APP的device token。这个接口最好定期调用，比如一天一次，或者一小时一次等等
	 */
	public List<Feedback> getFeedbacks();
}
