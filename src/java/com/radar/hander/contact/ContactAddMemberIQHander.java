package com.radar.hander.contact;

import java.util.List;

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
import com.zyt.web.after.friends.remote.ImCrmFriends;

public class ContactAddMemberIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.ADD_FRIENDS;
    private static final Logger log = LoggerFactory.getLogger(ContactAddMemberIQHander.class);
	public ContactAddMemberIQHander() {
		super("ContactAddMemberIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String fgId="";
        String friendUserId="";
        String friendDesc="";
        for (Object object : node) {
            Element elm = (Element)object;
            fgId=elm.attributeValue("fgId");
            friendUserId=elm.attributeValue("friendUserId");
            friendDesc=elm.attributeValue("friendDesc");
        }
        if(StringUtils.isEmpty(fgId) || StringUtils.isEmpty(friendUserId)){
        	replay.setType(IQ.Type.error);
        	log.info("添加联系人参数错误");
        }else{
        	ImCrmFriends imCrmFriends=new ImCrmFriends();
        	imCrmFriends.setFgId(fgId);
        	imCrmFriends.setFriendUserId(friendUserId);
        	imCrmFriends.setFriendDesc(friendDesc);
        	imCrmFriends=ContactAction.addMember(imCrmFriends);
            if(imCrmFriends!=null && StringUtils.isNotEmpty(imCrmFriends.getFriendId())){
            	replay.setChildElement("query", NAME_SPACE);
            	replay.getChildElement().addElement("friends").addAttribute("friendId",imCrmFriends.getFriendId());
            }else{
            	replay.setType(IQ.Type.error);
            	log.info("添加联系人失败");
            }
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
