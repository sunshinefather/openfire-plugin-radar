package com.radar.broadcast.group;

import java.util.List;

import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;

import com.radar.action.GroupAction;
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

	private static final String RESOURCE = "system"; // 表明是系统消息
	private static final Logger log = LoggerFactory.getLogger(GroupBroadcastTask.class);

	public GroupBroadcastTask(String message, String groupUid) {
		this.message = message;
		this.groupUid = groupUid;
	}

	@Override
	public void executeTask() throws Exception {
		Message sendMsg = new Message();
		sendMsg.setID(UUIDUtils.getUUID());
		Element sendTime = sendMsg.addChildElement("sendTime", "urn:xmpp:time");
		sendTime.addAttribute("stamp", System.currentTimeMillis() + "");
		
		sendMsg.setBody(message);
		sendMsg.setType(Message.Type.groupchat);
		sendMsg.setFrom(groupUid + "@" + HixinUtils.getGroupDomain() + "/"+ RESOURCE);

		List<String> item =GroupAction.getGroupMemberListWithUserName(groupUid);
		if (item == null || item.isEmpty()) {
			return;
		}

		for (String jid : item) {
			User user = null;
			try {
				user = XMPPServer.getInstance().getUserManager().getUser(jid);
			} catch (UserNotFoundException e) {
				log.debug(jid+" User Not Found.", e);
			}
			if (user != null) {
				sendMsg.setTo(jid + "@" + HixinUtils.getDomain());
				XMPPServer.getInstance().getSessionManager().userBroadcast(jid, sendMsg);
			}
		}
	}
}