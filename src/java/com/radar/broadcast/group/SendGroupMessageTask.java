package com.radar.broadcast.group;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.openfire.XMPPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import com.radar.action.GroupAction;
import com.radar.pool.QueueTask;
import com.radar.utils.HixinUtils;
/**
 * 群聊消息发送队列
 * @ClassName:  SendGroupMessageTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:33:22
 */
public class SendGroupMessageTask implements QueueTask{

	private Message message;
	private static final Logger log = LoggerFactory.getLogger(SendGroupMessageTask.class);
	
	public SendGroupMessageTask(Message message){
		this.message = message;
	}
	
	@Override
	public void executeTask() throws Exception {
		 try
	        {
			 List<String> item = queryGroupMember(message.getTo().getNode());
	            for (String userName : item)
	            {
	                if (!message.getFrom().getNode().equalsIgnoreCase(userName))
	                {
	                    this.sendGroupMessage(userName, message.createCopy());
	                }
	            }
	        } catch (Exception e)
	        {
	            log.error("发送群消息失败,message=" + message.toString(), e);
	        }
	}

	/**
     * 根据群组id查询群组成员
     * 
     * @param groupUid 群组id
     * @return
     * @throws Exception
     */
    private List<String> queryGroupMember(String groupUid) throws Exception
    {
        List<String>  item = GroupAction.getGroupMemberListWithUserName(groupUid);
        return item == null ? new ArrayList<String>(0) : item;
    }

    /**
     * 发送群组消息
     * 
     * @param user 用户信息
     * @param groupMessage 群组信息
     */
    private void sendGroupMessage(String userName, Message groupMessage)
    {
        try
        {
	        groupMessage.setFrom(new JID(groupMessage.getTo().getNode(), HixinUtils.getGroupDomain(), groupMessage.getFrom().getNode()));
	        groupMessage.setTo(userName+"@"+HixinUtils.getDomain());
	        groupMessage.setID(groupMessage.getID());
	        XMPPServer.getInstance().getSessionManager().userBroadcast(userName, groupMessage);
        } catch (Exception e)
        {
        	log.error("message send Exception:" + e.getMessage());
        	log.debug("message=" + groupMessage);
            throw e;
        }
    }
}