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
import com.mdks.imcrm.bean.GroupMember;
import com.radar.action.GroupAction;
import com.radar.common.IqConstant;
/**
 * 移除群成员,当userLoginName值为:allUsers清空除群创建者以外的所有人
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
    	replay.setChildElement("query", NAME_SPACE);
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
        if(StringUtils.isEmpty(userLoginName)){
        	userLoginName=packet.getFrom().getNode();
        }
        if(StringUtils.isNotEmpty(groupUserId) || (StringUtils.isNotEmpty(userLoginName) && StringUtils.isNotEmpty(groupid))){
        	if("allUsers".equals(userLoginName)){//userLoginName为allUsers清空除群管理员以外的所有成员
            	userLoginName=null;
            }
        	GroupMember GroupMember=new GroupMember();
        	GroupMember.setGroupUserId(groupUserId);
        	GroupMember.setGroupId(groupid);
        	GroupMember.setUserName(userLoginName);
        	boolean rt=GroupAction.delMember(GroupMember,packet.getFrom().getNode());
        	if(!rt){
        		replay.setType(IQ.Type.error);
            	log.error("移除群成员失败",packet.toXML());
            }
        }else{
        	replay.setType(IQ.Type.error);
        	log.error("移除群成员参数错误",packet.toXML());
        }
        return replay;
    }

    @Override
    public IQHandlerInfo getInfo()
    {
        return info;
    }
}
