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
import com.mdks.imcrm.bean.Friends;
import com.radar.action.ContactAction;
import com.radar.common.IqConstant;

public class ContactReNameMemberIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.RENAMR_MEMBER;
    private static final Logger log = LoggerFactory.getLogger(ContactReNameMemberIQHander.class);
	public ContactReNameMemberIQHander() {
		super("ContactReNameMemberIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String friendId="";
        String friendDesc="";
        for (Object object : node) {
            Element elm = (Element)object;  
            friendId=elm.attributeValue("friendId");
            friendDesc=elm.attributeValue("friendDesc");
        }
        if(StringUtils.isEmpty(friendId) && StringUtils.isEmpty(friendDesc)){
        	replay.setType(IQ.Type.error);
        	log.info("修改联系人备注参数错误");
        }else{
        	Friends imCrmFriends=new Friends();
        	imCrmFriends.setFriendId(friendId);
        	imCrmFriends.setFriendDesc(friendDesc);
        	boolean rt=ContactAction.reNameMember(imCrmFriends);
            if(!rt){
               	replay.setType(IQ.Type.error);
            	log.info("修改联系人备注失败");
            }
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
