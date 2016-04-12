package com.radar.interceptor;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;
import com.radar.action.MessageLogTask;
import com.radar.bean.MsgInfoEnttity.Type;
import com.radar.broadcast.group.SendGroupMessageTask;
import com.radar.ios.PushMessageTask;
import com.radar.pool.ThreadPool;
import com.radar.utils.HixinUtils;
/**
 * 消息记录拦截器
 * @ClassName:  SearchEngineInterceptor   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午4:15:04
 */
public class SearchEngineInterceptor implements PacketInterceptor {

	private static final Integer CHAT_TYPE = 1;//1（人对人）
	private static final Integer GROUP_TYPE = 2;//2（群组）
	private static final Logger log = LoggerFactory.getLogger(SearchEngineInterceptor.class);
	
	@Override
	public void interceptPacket(Packet packet, Session session,
			boolean incoming, boolean processed) throws PacketRejectedException {
		
		// 程序执行中；是否为结束或返回状态（是否是用户发送的消息）
		if (processed || !incoming) {
			return;
		}
		
		//过滤系统群消息分发后的数据包
		if(packet.getFrom() != null && StringUtils.isNotEmpty(packet.getFrom().getDomain()) && packet.getFrom().getDomain().startsWith(HixinUtils.getGroupPrefix())){
			return;
		}

		if (packet instanceof Message) {
			// 接收人不为空（可能接收人就是服务器）过滤回执消息
			Message message = (Message)packet;
			if(Message.Type.normal.equals(message.getType()) || HixinUtils.MESSAGE_RECEIVED.equals(message.getSubject()))
				return ;
			Long currentTimeMillis = System.currentTimeMillis();
			if(StringUtils.isNotBlank(message.getID())){
				receiptIQ(message.getFrom() ,message.getID());
			}
			PacketExtension _pke = message.getExtension("sendTime", "urn:xmpp:time");
			if(_pke==null){
				PacketExtension pke=new PacketExtension("sendTime", "urn:xmpp:time");
				pke.getElement().addElement("stamp").setText(currentTimeMillis+"");
				message.addExtension(pke);
			}
			//消息处理：1、保存聊天记录；2、IOS设备push消息；3、群消息分发
			this.handerMessageInfo(message.createCopy());
		}
	}
	
	/**
	 * 消息回执
	 * @param from 发送者JID
	 * @param messageID
	 */
	private void receiptIQ(JID from, String messageID){
		Message message = new Message();
		message.setTo(from);
		message.setFrom(new JID("serverAlert@"+HixinUtils.getDomain()));
		message.setSubject(HixinUtils.MESSAGE_RECEIVED);
		message.setBody(messageID);
		XMPPServer.getInstance().getMessageRouter().route(message);
	}
	
	/**
	 * 处理 message信息 message信息这里要处理两种：一种普通chat 一种groupchat ，groupchat的to是自定义的表
	 * 
	 * @param packet 数据包
	 * @param incoming  true表示发送方
	 * @param processed
	 * @param session 当前用户session
	 */
	private void handerMessageInfo(Message message) {
		log.debug("Message:"+message.toString());
		
		//一对一聊天，单人模式
		if (message.getType() == Message.Type.chat) { 
			// 发送文件的时候，不保存发送中的message
			if (!HixinUtils.isSending(message.getSubject())) {
				//保存聊天记录
				ThreadPool.addWork(new MessageLogTask(CHAT_TYPE, SearchEngineInterceptor.messageType(message.getSubject()), message.createCopy()));
				
				//IOS推送消息
				ThreadPool.addWork(new PushMessageTask(message.createCopy()));
			}
		} 
		// 群聊天，多人模式
		else if (message.getType() == Message.Type.groupchat) {
			
			//群消息分发
			ThreadPool.addWork(new SendGroupMessageTask(message.createCopy()));
			// 发送资源文件的时候，不保存发送中的message
			if (!HixinUtils.isSending(message.getSubject())) {            
				//保存群聊天记录
				ThreadPool.addWork(new MessageLogTask(GROUP_TYPE, SearchEngineInterceptor.messageType(message.getSubject()), message.createCopy()));
				
				//IOS推送消息
                ThreadPool.addWork(new PushMessageTask(message.createCopy()));
			}
		}
	}
	
	/**
	 * 提示消息
	 * @param subject 主题类型
	 * @param message 消息内容
	 * @return 提示消息内容
	 */
	private static Type messageType(String subject){
		if ("image".equals(subject)) {
			return Type.IMAGE;
		}else if ("sound".equals(subject)) {
			return Type.SOUND;
		} else if ("attachment".equals(subject)) {
			return Type.FILE;
		}else if ("other".equals(subject)) {
			return Type.OTHER;
		}else{
			return Type.MESSAGE;
		    }
	}
}