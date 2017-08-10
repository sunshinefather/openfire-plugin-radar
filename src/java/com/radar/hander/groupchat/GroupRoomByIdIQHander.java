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
 * 群组详细信息
 * @ClassName:  GroupRoomByIdIQHander   
 *    
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomByIdIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.GET_GROUP_ROOM;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomByIdIQHander.class);
    
    public GroupRoomByIdIQHander(){
        super("GroupRoomByIdIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
    	String groupId=null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupId = elm.attributeValue("groupId");
        }
        
        if(StringUtils.isEmpty(groupId)){
        	replay.setType(IQ.Type.error);
        	log.info("获取群组详细信息参数错误");
        }else {
        	GroupRoom GroupRoom=GroupAction.getGroupRoomById(groupId);
        	if(GroupRoom!=null){
        		replay.getChildElement().addElement("groupRoom")
            	.addAttribute("groupId",GroupRoom.getGroupId())
            	.addAttribute("groupName",GroupRoom.getGroupName())
            	.addAttribute("createUserId",GroupRoom.getCreateUserId())
            	.addAttribute("memberSize",GroupRoom.getMemberSize())
            	.addAttribute("roomPassword",GroupRoom.getRoomPassword())
            	.addAttribute("groupType",GroupRoom.getGroupType())
            	.addAttribute("groupDesc",GroupRoom.getGroupDesc())
            	.addAttribute("extension1",GroupRoom.getExtension1())
            	.addAttribute("extension2",GroupRoom.getExtension2());
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
