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

public class ContactDellFriendsGroupIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.DEL_FRIENDS_GROUPS;
    private static final Logger log = LoggerFactory.getLogger(ContactDellFriendsGroupIQHander.class);
	public ContactDellFriendsGroupIQHander() {
		super("ContactDellFriendsGroupIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String fgId="";
        for (Object object : node) {
            Element elm = (Element)object;  
            fgId=elm.attributeValue("fgId");
        }
        if(StringUtils.isEmpty(fgId)){
        	replay.setType(IQ.Type.error);
        	log.info("删除分组参数错误");
        }else{
            FriendGroups imCrmFriendGroups=new FriendGroups();
            imCrmFriendGroups.setFgId(fgId);
            boolean rt=ContactAction.delFriendsGroup(imCrmFriendGroups);
            if(!rt){
            	replay.setType(IQ.Type.error);
            	log.info("删除分组失败");
            }
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
