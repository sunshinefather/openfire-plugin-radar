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
 * 群成员列表(已过时兼容老版本)
 * @ClassName:  GroupRoomMemberListIQHander   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:02:41
 */
public class GroupRoomMemberListIQHander extends IQHandler
{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.GROUP_ROOM_MEMBER_LIST;
    private static final Logger log = LoggerFactory.getLogger(GroupRoomMemberListIQHander.class);
    
    public GroupRoomMemberListIQHander(){
        super("GroupRoomMemberListIQHander");
        info = new IQHandlerInfo("query", NAME_SPACE);
    }
    
    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
    	IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
        String groupId = null;
        Element query = packet.getChildElement();
        List<?> node =  query.elements();
        for (Object object : node) {
            Element elm = (Element)object;
            groupId = elm.attributeValue("groupId");
        }
        
        if(StringUtils.isEmpty(groupId)){
        	replay.setType(IQ.Type.error);
        	log.info("群成员列表参数错误");
        }else {
        	@SuppressWarnings("deprecation")
			List<GroupMember> groupMemberlist =GroupAction.getGroupMemberList(groupId);
        	if(groupMemberlist!=null && groupMemberlist.size()>0){
        		for(GroupMember GroupMember:groupMemberlist){
                	replay.getChildElement().addElement("item")
                	.addAttribute("groupUserId",GroupMember.getGroupUserId())
                	.addAttribute("groupid",GroupMember.getGroupId())
                	.addAttribute("userAlias",GroupMember.getUserAlias())
                	.addAttribute("userHead",GroupMember.getUserHead())
                	.addAttribute("userName",GroupMember.getNickName())
                	.addAttribute("userLoginName",GroupMember.getUserName())
                	.addAttribute("userState",GroupMember.getUserState())
                	.addAttribute("userId",GroupMember.getUserId())
                	.addAttribute("extension1",GroupMember.getExtension1())
                	.addAttribute("extension2",GroupMember.getExtension2())
                	.addAttribute("groupAdmin",GroupMember.getGroupAdmin());
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