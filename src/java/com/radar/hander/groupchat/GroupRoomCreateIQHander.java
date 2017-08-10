package com.radar.hander.groupchat;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import com.mdks.imcrm.bean.GroupRoom;
import com.radar.action.GroupAction;
import com.radar.common.IqConstant;
/**
 * 创建群组
 * @ClassName:  GroupRoomCreateIQHander   
 *    
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomCreateIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.ADD_GROUP_ROOM;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomCreateIQHander.class);
    
    public GroupRoomCreateIQHander(){
        super("GroupRoomCreateIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
        String groupName = null;
        String createUserId  = null;
        String groupType=null;
        String groupDesc=null;
        String memberSize=null;
        String roomPassword=null;
        String extension1=null;
        String extension2=null;
        String userId=null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupName = elm.attributeValue("groupname");
            createUserId = elm.attributeValue("createUserId");
            groupType=elm.attributeValue("groupType");
            groupDesc=elm.attributeValue("groupDesc");
            memberSize=elm.attributeValue("memberSize");
            roomPassword=elm.attributeValue("roomPassword");
            extension1=elm.attributeValue("extension1");
            extension2=elm.attributeValue("extension2");
            userId=elm.attributeValue("userId");
        }
        if(StringUtils.isNotBlank(userId)){
        	createUserId = createUserId+","+userId;
        }
        
        if(StringUtils.isEmpty(groupName) || StringUtils.isEmpty(createUserId)){
        	replay.setType(IQ.Type.error);
        	log.error("创建群组参数错误,xml="+packet.toXML());
        }else {
        	GroupRoom GroupRoom=new GroupRoom();
        	GroupRoom.setGroupName(groupName);
        	GroupRoom.setCreateUserId(createUserId);
        	if(StringUtils.isNotEmpty(groupType)){
            GroupRoom.setGroupType(groupType);
        	}
        	GroupRoom.setGroupDesc(groupDesc);
        	if(StringUtils.isNotEmpty(memberSize)){
        		GroupRoom.setMemberSize(memberSize);
            }
        	GroupRoom.setRoomPassword(roomPassword);
        	GroupRoom.setExtension1(extension1);
        	GroupRoom.setExtension2(extension2);
        	GroupRoom=GroupAction.createGroupRoom(GroupRoom);
        	if(GroupRoom!=null && StringUtils.isNotEmpty(GroupRoom.getGroupId())){
            	replay.getChildElement().addElement("groupRoom")
            	.addAttribute("groupId",GroupRoom.getGroupId())
            	.addAttribute("groupName",GroupRoom.getGroupName())
            	.addAttribute("createUserId",GroupRoom.getCreateUserId())
            	.addAttribute("memberSize",GroupRoom.getMemberSize())
            	.addAttribute("roomPassword",GroupRoom.getRoomPassword())
            	.addAttribute("groupDesc",GroupRoom.getGroupDesc())
            	.addAttribute("groupType",GroupRoom.getGroupType())
            	.addAttribute("extension1",GroupRoom.getExtension1())
            	.addAttribute("extension2",GroupRoom.getExtension2());
            }else{
            	replay.setType(IQ.Type.error);
            	log.error("创建群组失败,xml="+packet.toXML());
		    }
        }
        return replay;
    }

    @Override
    public IQHandlerInfo getInfo()
    {
        return info;
    }
}
