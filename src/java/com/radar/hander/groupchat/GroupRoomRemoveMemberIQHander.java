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
 * 移除群成员
 * @ClassName:  GroupRoomRemoveMemberIQHander   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomRemoveMemberIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.DEL_GROUP_ROOM_MEMBER;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomRemoveMemberIQHander.class);
    
    public GroupRoomRemoveMemberIQHander(){
        super("GroupRoomRemoveMemberIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
        String groupUserId = null;
        String userLoginName=null;
        String groupid=null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupUserId = elm.attributeValue("groupUserId");
            userLoginName = elm.attributeValue("userLoginName");
            groupid=elm.attributeValue("groupId");
        }
        
        if(StringUtils.isNotEmpty(groupUserId) || (StringUtils.isNotEmpty(userLoginName) && StringUtils.isNotEmpty(groupid))){
        	ImCrmGroupMember imCrmGroupMember=new ImCrmGroupMember();
        	imCrmGroupMember.setGroupUserId(groupUserId);
        	imCrmGroupMember.setGroupId(groupid);
        	imCrmGroupMember.setUserName(userLoginName);
        	boolean rt=GroupAction.delMember(imCrmGroupMember,packet.getFrom().getNode());
        	if(!rt){
        		replay.setType(IQ.Type.error);
            	log.info("移除群成员失败");
            }
        }else{
        	replay.setType(IQ.Type.error);
        	log.info("移除群成员参数错误");
        }
        return replay;
    }

    @Override
    public IQHandlerInfo getInfo()
    {
        return info;
    }
}
