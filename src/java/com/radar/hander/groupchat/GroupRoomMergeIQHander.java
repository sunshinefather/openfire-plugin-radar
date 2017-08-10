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
 * 修改群组
 * @ClassName:  GroupRoomMergeIQHander   
 *    
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomMergeIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.UPDATE_GROUP_ROOM;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomMergeIQHander.class);
    
    public GroupRoomMergeIQHander(){
        super("GroupRoomMergeIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
    	String groupId=null;
        String groupName = null;
        String groupDesc=null;
        String memberSize=null;
        String roomPassword=null;
        String groupType=null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupId = elm.attributeValue("groupId");
            groupName = elm.attributeValue("groupName");
            groupDesc=elm.attributeValue("groupDesc");
            memberSize=elm.attributeValue("memberSize");
            roomPassword=elm.attributeValue("roomPassword");
            groupType=elm.attributeValue("groupType");
        }
        if(StringUtils.isEmpty(groupId)){
        	replay.setType(IQ.Type.error);
        	log.info("修改群组参数错误");
        }else {
        	GroupRoom GroupRoom=new GroupRoom();
        	GroupRoom.setGroupName(groupName);
        	GroupRoom.setGroupId(groupId);
        	GroupRoom.setGroupDesc(groupDesc);
        	GroupRoom.setRoomPassword(roomPassword);
        	GroupRoom.setGroupType(groupType);
        	if(StringUtils.isNotEmpty(memberSize)){
        		GroupRoom.setMemberSize(memberSize);
            }
        	GroupRoom.setRoomPassword(roomPassword);
        	boolean rt=GroupAction.mergeGroupRoom(GroupRoom);
        	if(!rt){
            	replay.setType(IQ.Type.error);
            	log.info("修改群组参数失败");
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
