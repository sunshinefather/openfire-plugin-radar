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

public class MessageListIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.MESSAGE_LIST;
    private static final Logger log = LoggerFactory.getLogger(MessageListIQHander.class);
	public MessageListIQHander() {
		super("MessageListIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        String targetId="";
        int pageNo=0;
        int pageSize=10;
        String chatType="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            targetId=elm.attributeValue("targetId");
            chatType=elm.attributeValue("chatType");
            String pageNoStr=elm.attributeValue("pageNo");
            String pageSizeStr=elm.attributeValue("pageSize");
            try{
            if(StringUtils.isNotEmpty(pageNoStr)){
            	pageNo=Integer.valueOf(pageNoStr);
            }
            if(StringUtils.isNotEmpty(pageSizeStr)){
            	pageSize=Integer.valueOf(pageSizeStr);
            }
            }catch(NumberFormatException e){
            	log.info("参数类型传入错误");
            }
            if(StringUtils.isEmpty(userId)){
            	userId=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(targetId)|| StringUtils.isEmpty(chatType)){
        	replay.setType(IQ.Type.error);
        	log.info("获取聊天记录参数错误");
        }else {
        	String msglist=MsgAction.findMessageList(userId, targetId, pageNo, pageSize, chatType);
        	if(StringUtils.isEmpty(msglist)){
        		replay.setType(IQ.Type.error);
        		log.info("获取聊天记录失败");
        	}else{
            	replay.getChildElement().addElement("messagesJson").addCDATA(msglist);
        	}
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
