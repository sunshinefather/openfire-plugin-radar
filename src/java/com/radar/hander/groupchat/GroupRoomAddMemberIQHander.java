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
import com.radar.action.GroupAction;
import com.radar.common.IqConstant;
import com.zyt.web.after.grouproom.remote.ImCrmGroupMember;
/**
 * 添加群成员
 * @ClassName:  GroupRoomAddMemberIQHander   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomAddMemberIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.ADD_GROUP_ROOM_MEMBER;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomAddMemberIQHander.class);
    
    public GroupRoomAddMemberIQHander(){
        super("GroupRoomAddMemberIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
        String groupId = null;
        String userId  = null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupId = elm.attributeValue("groupId");
            userId = elm.attributeValue("userId");
        }
        
        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(userId)){
        	replay.setType(IQ.Type.error);
        	log.info("添加群成员参数错误");
        }else {
        	ImCrmGroupMember imCrmGroupMember=new ImCrmGroupMember();
        	imCrmGroupMember.setGroupId(groupId);
        	imCrmGroupMember.setUserName(userId);
        	imCrmGroupMember=GroupAction.addMember(imCrmGroupMember,packet.getFrom().getNode());
        	if(imCrmGroupMember!=null && StringUtils.isNotEmpty(imCrmGroupMember.getGroupUserId())){
            	replay.getChildElement().addElement("groupRoomMember")
            	.addAttribute("groupUserId",imCrmGroupMember.getGroupUserId());
            }else{
            	replay.setType(IQ.Type.error);
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
