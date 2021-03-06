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
import com.zyt.web.after.grouproom.remote.ImCrmGroupRoom;
/**
 * 拉取群组列表
 * @ClassName:  GroupRoomListIQHander   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomListIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.GROUP_ROOM_LIST;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomListIQHander.class);
    
    public GroupRoomListIQHander(){
        super("GroupRoomListIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
        String userId = null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            userId = elm.attributeValue("userId");
        }
        
        if(StringUtils.isEmpty(userId)){
        	replay.setType(IQ.Type.error);
        	log.info("获取群组列表参数错误");
        }else {
        	List<ImCrmGroupRoom> list=GroupAction.findGroupRoomList(userId, "");
        	if(list!=null && list.size()>0){
            	for(ImCrmGroupRoom imCrmGroupRoom:list){
            		replay.getChildElement().addElement("item")
                	.addAttribute("groupId",imCrmGroupRoom.getGroupId())
                	.addAttribute("groupName",imCrmGroupRoom.getGroupName())
                	.addAttribute("createUserId",imCrmGroupRoom.getCreateUserId())
                	.addAttribute("memberSize",imCrmGroupRoom.getMemberSize())
                	.addAttribute("roomPassword",imCrmGroupRoom.getRoomPassword())
                	.addAttribute("groupType",imCrmGroupRoom.getGroupType())
                	.addAttribute("groupDesc",imCrmGroupRoom.getGroupDesc())
                	.addAttribute("extension1",imCrmGroupRoom.getExtension1())
                	.addAttribute("extension2",imCrmGroupRoom.getExtension2());
            	}
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
