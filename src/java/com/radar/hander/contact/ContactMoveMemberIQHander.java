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

public class ContactMoveMemberIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.MOVE_MEMBER;
    private static final Logger log = LoggerFactory.getLogger(ContactMoveMemberIQHander.class);
	public ContactMoveMemberIQHander() {
		super("ContactMoveMemberIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String friendId="";
        String fgId="";
        for (Object object : node) {
            Element elm = (Element)object;  
            friendId=elm.attributeValue("friendId");
            fgId=elm.attributeValue("fgId");
        }
        if(StringUtils.isEmpty(friendId) && StringUtils.isEmpty(fgId)){
        	replay.setType(IQ.Type.error);
        	log.info("转移联系人参数错误");
        }else{
        	ImCrmFriends imCrmFriends=new ImCrmFriends();
        	imCrmFriends.setFriendId(friendId);
        	imCrmFriends.setFgId(fgId);
        	boolean rt=ContactAction.moveMember(imCrmFriends);
            if(!rt){
               	replay.setType(IQ.Type.error);
            	log.info("转移联系人失败");
            }
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
