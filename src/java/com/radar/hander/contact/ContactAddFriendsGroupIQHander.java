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
import com.mdks.imcrm.bean.FriendGroups;
import com.radar.action.ContactAction;
import com.radar.common.IqConstant;

public class ContactAddFriendsGroupIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.ADD_FRIENDS_GROUPS;
    private static final Logger log = LoggerFactory.getLogger(ContactAddFriendsGroupIQHander.class);
	public ContactAddFriendsGroupIQHander() {
		super("ContactAddFriendsGroupIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
    	replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        String fgName="";
        String extension1="";
        String extension2="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            fgName=elm.attributeValue("fgName");
            extension1=elm.attributeValue("extension1");
            extension2=elm.attributeValue("extension2");
        }
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(fgName)){
        	replay.setType(IQ.Type.error);
        	log.info("创建分组参数错误");
        }else{
            FriendGroups imCrmFriendGroups=new FriendGroups();
            imCrmFriendGroups.setExtension1(extension1);
            imCrmFriendGroups.setExtension2(extension2);
            imCrmFriendGroups.setFgName(fgName);
            imCrmFriendGroups.setUserId(userId);
            imCrmFriendGroups=ContactAction.addFriendsGroup(imCrmFriendGroups);
            if(imCrmFriendGroups!=null && StringUtils.isNotEmpty(imCrmFriendGroups.getFgId())){
            	replay.getChildElement().addElement("friendGroup").addAttribute("fgId",imCrmFriendGroups.getFgId()).addAttribute("fgName", imCrmFriendGroups.getFgName());
            }else{
            	replay.setType(IQ.Type.error);
            	log.info("创建分组失败");
            }
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
