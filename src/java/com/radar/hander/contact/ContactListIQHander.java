package com.radar.hander.contact;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.ContactAction;
import com.radar.common.IqConstant;
import com.zyt.web.after.friends.remote.ImCrmFriendGroups;
import com.zyt.web.after.friends.remote.ImCrmFriends;

public class ContactListIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.FRIENDS_LIST;
    private static final Logger log = LoggerFactory.getLogger(ContactListIQHander.class);
	public ContactListIQHander() {
		super("ContactListIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        String userFrom="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            userFrom=elm.attributeValue("userFrom");
            if(StringUtils.isEmpty(userId)){
            	userId=packet.getFrom().getNode();
            }
        }
		Map<ImCrmFriendGroups, List<ImCrmFriends>> map=ContactAction.friendsList(userId,userFrom);
		if(map!=null && map.size()>0){
			for(ImCrmFriendGroups imfg:map.keySet()){
				Element elm=replay.getChildElement()
						.addElement("friendGroup")
						.addAttribute("fgId",imfg.getFgId())
						.addAttribute("fgName",imfg.getFgName());
				List<ImCrmFriends> list=map.get(imfg);
				if(list!=null && list.size()>0){
					for(ImCrmFriends imfds:list){
						elm.addElement("friends")
						.addAttribute("friendId",imfds.getFriendId())
						.addAttribute("friendUserId",imfds.getFriendUserId())
						.addAttribute("userLoginName",imfds.getFriendUserLoginName())
						.addAttribute("friendUserName", imfds.getFriendUserName())
						.addAttribute("friendUserHead", imfds.getFriendUserHead())
						.addAttribute("friendDesc", imfds.getFriendDesc())
						.addAttribute("extension1",imfds.getExtension1())
						.addAttribute("extension2",imfds.getExtension2());
					}
				}
			}
		}else{
			log.info("IMCRM返回了NULL数据");
		}
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
