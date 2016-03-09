package com.radar.hander.pushconfig;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.IQ.Type;

import com.radar.action.PushConfigAction;
import com.radar.common.IqConstant;
import com.zyt.web.after.push.remote.ImCrmPushConfig;

public class PushConfigSwitchIQHander extends IQHandler {
	
	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.PUSH_CONFIG;
    private static final Logger log = LoggerFactory.getLogger(PushConfigSwitchIQHander.class);
	public PushConfigSwitchIQHander() {
		super("PushConfigSwitchIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
        replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userName="";
        String groupId="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userName=elm.attributeValue("userName");
            groupId=elm.attributeValue("groupId");
        }
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(groupId)){
        	replay.setType(Type.error);
        	log.info("切换通知推送失败:参数错误");
        	return replay;
        }
        ImCrmPushConfig pushconfig =new ImCrmPushConfig();
        pushconfig.setGroupId(groupId);
        pushconfig.setUserName(userName);
        boolean flag= PushConfigAction.switchPush(pushconfig);
        if(!flag){
        	replay.setType(Type.error);
        	replay.getChildElement().addElement("status").setText("false");
        	log.info("切换通知推送失败");	
        }else{
        	replay.setChildElement("query", NAME_SPACE);
        	replay.getChildElement().addElement("status").setText("true");
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
