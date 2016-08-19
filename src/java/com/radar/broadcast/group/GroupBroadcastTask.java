package com.radar.broadcast.group;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import com.radar.pool.QueueTask;
import com.radar.utils.HixinUtils;
import com.radar.utils.UUIDUtils;
/**
 * 系统发送到群的消息 队列
 * @ClassName:  GroupBroadcastTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年1月28日 下午5:36:03
 */
public class GroupBroadcastTask implements QueueTask {

	private String message;
	private String groupUid;
	private String userName;

	private static final String RESOURCE = "system"; // 表明是系统消息
	private static final Logger log = LoggerFactory.getLogger(GroupBroadcastTask.class);

	public GroupBroadcastTask(String message, String groupUid,String userName) {
		this.message = message;
		this.groupUid = groupUid;
		this.userName = userName;
	}

	@Override
	public void executeTask() throws Exception {
		Message sendMsg = new Message();
		sendMsg.setID(UUIDUtils.getUUID());
		Element sendTime = sendMsg.addChildElement("sendTime", "urn:xmpp:time");
		sendTime.addAttribute("stamp", System.currentTimeMillis() + "");
		
		sendMsg.setBody(message);
		sendMsg.setSubject("removeGroup");
		sendMsg.setType(Message.Type.groupchat);
		sendMsg.setFrom(groupUid + "@" + HixinUtils.getGroupDomain() + "/"+ RESOURCE);
        if(userName!=null && StringUtils.isNotEmpty(userName)){
        	String[] userNames=userName.split("[,]");
    		for (String jid : userNames) {
    			User user = null;
    			try {
    				user = XMPPServer.getInstance().getUserManager().getUser(jid);
        			if (user != null) {
        				sendMsg.setTo(jid+"@"+HixinUtils.getDomain());
        				XMPPServer.getInstance().getSessionManager().userBroadcast(jid,sendMsg);
        			}
    			} catch (UserNotFoundException e) {
    				log.error("@sunshine:系统发送到群的消息失败:"+jid+"不存在",e);
    			}
    		}
        }
	}
}