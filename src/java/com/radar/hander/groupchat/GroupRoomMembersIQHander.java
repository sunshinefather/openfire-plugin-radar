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
/**
 * 群成员列表
 * @ClassName:  GroupRoomMemberListIQHander   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomMembersIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.GROUP_ROOM_MEMBERS;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomMembersIQHander.class);
    
    public GroupRoomMembersIQHander(){
        super("GroupRoomMembersIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
        String groupId = null;
        String pageSize = null;
        String pageNo = null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupId = elm.attributeValue("groupId");
            pageSize = elm.attributeValue("pageSize");
            pageNo = elm.attributeValue("pageNo");
        }
        
        if(StringUtils.isEmpty(groupId)){
        	replay.setType(IQ.Type.error);
        	log.info("群成员列表参数错误");
        }else {
        	String memberList =GroupAction.getGroupMemberList(groupId,pageSize,pageNo);
        	if(StringUtils.isBlank(memberList)){
        		replay.setType(IQ.Type.error);
        		log.info("获取群成员列表失败");
        	}else{
            	replay.getChildElement().addElement("groupMembersJson").addCDATA(memberList);
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