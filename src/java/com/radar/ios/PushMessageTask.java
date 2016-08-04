package com.radar.ios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;

import com.radar.pool.QueueTask;
/**
 * ios 消息推送
 * @ClassName:  PushMessageTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:07:29
 */
public class PushMessageTask implements QueueTask {

	private Message message;
    private String accepterType;
    private String appName;
	private static final Logger log = LoggerFactory.getLogger(PushMessageTask.class);

	public PushMessageTask(Message message){
		this.message = message;
	}
	public PushMessageTask(String appName,String accepterType,Message message){
		this.message = message;
		this.appName=appName;
		this.accepterType=accepterType;
	}
	@Override
	public void executeTask() throws Exception {
		// IOS推送消息
		try {
			if(Message.Type.chat.equals(message.getType())){
				PushMessage.pushChatMessage(message);
			}else if (Message.Type.groupchat.equals(message.getType())) {
				PushMessage.pushGroupChatMessage(message);
			}else if(Message.Type.headline.equals(message.getType())){
				PushMessage.pushNoticeMessage(appName,accepterType,message);
			}
		} catch (Exception e) {
			log.error("推送消息失败：" + e.getMessage());
		}
	}
}
