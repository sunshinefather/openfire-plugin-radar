package com.radar.action;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;

import com.radar.bean.MsgInfoEnttity.Type;
import com.radar.pool.QueueTask;
import com.zyt.web.after.messages.remote.ImCrmMessage;
/**
 * 保存消息记录
 * @ClassName:  MessageLogTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:04:15
 */
public class MessageLogTask implements QueueTask {
	
	private int chatType;
	private Type contentType;
	private Long sendDate;
	private String sender;
	private String accepter;
	private String messageInfo;
	private ImCrmMessage imCrmMessage;
	private static final Logger log = LoggerFactory.getLogger(MessageLogTask.class);
    
	public MessageLogTask (int type, Type contentType, Message message){
		this.chatType = type;
		this.contentType = contentType;
		if(message.getChildElement("sendTime", "urn:xmpp:time") != null){
			String timestr=message.getChildElement("sendTime", "urn:xmpp:time").elementTextTrim("stamp");
			if(StringUtils.isNotEmpty(timestr)){
				this.sendDate = Long.valueOf(timestr);
			}else{
				this.sendDate=System.currentTimeMillis();
			}
		}else {
			this.sendDate = System.currentTimeMillis();
		}
		this.sender = message.getFrom().getNode();
		this.accepter = message.getTo().getNode();
		this.messageInfo = message.getBody();
		imCrmMessage=new ImCrmMessage();
		imCrmMessage.setAccepter(accepter);
 		imCrmMessage.setSender(sender);
 		imCrmMessage.setSendDate(String.valueOf(sendDate));
 		imCrmMessage.setChatType(String.valueOf(chatType));
 		imCrmMessage.setContentType(String.valueOf(this.contentType.getCode()));
 		imCrmMessage.setMessageInfo(messageInfo);
 		if(Type.SOUND.equals(contentType)){
 			PacketExtension pkg=message.getExtension("sound","sound:extension:size");
 			  if(pkg!=null && StringUtils.isNotEmpty(pkg.getElement().elementTextTrim("size"))){
 				 imCrmMessage.setMessagesize(pkg.getElement().elementTextTrim("size"));
 			  }
 		}
	}

	@Override
	public void executeTask() throws Exception {
		if(StringUtils.isNotBlank(messageInfo) && StringUtils.isNotBlank(sender) && StringUtils.isNotBlank(accepter)){
			boolean rt=MsgAction.saveMessage(imCrmMessage);
	 		if(rt){
	 			log.debug(sendDate + " 聊天记录存入成功：" + sender);
	 		}else{
	 			log.error(sendDate+ ":聊天记录保存失败:" + sender);
	 		}
		 }else{
			 log.debug("@sunshine 空错误, 发送者:" + sender);
		 }
		
	 	}
}