package com.radar.hander.message;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.MsgAction;
import com.radar.common.IqConstant;

public class MessageDelIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.DEL_MESSAGE_IDS;
    private static final Logger log = LoggerFactory.getLogger(MessageDelIQHander.class);
	public MessageDelIQHander() {
		super("MessageDelIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        String messageIds="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            messageIds=elm.attributeValue("messageIds");
            if(StringUtils.isEmpty(userId)){
            	userId=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(messageIds)){
        	replay.setType(IQ.Type.error);
        	log.info("撤回聊天记录参数错误");
        }else {
        	boolean bool=MsgAction.delMessage(userId, messageIds);
        	if(bool){
            	replay.getChildElement().addElement("messageIds").setText(messageIds);
        	}else{
            	replay.setType(IQ.Type.error);
            	log.info("撤回聊天记录失败");
        	}
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
