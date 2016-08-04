package com.radar.hander.notice;

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

import com.radar.action.NoticeAction;
import com.radar.common.IqConstant;

public class NoticeUpdateStateIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.UPDATE_NOTICE_STATE;
    private static final Logger log = LoggerFactory.getLogger(NoticeUpdateStateIQHander.class);
	public NoticeUpdateStateIQHander() {
		super("NoticeUpdateStateIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String accepter="";
        String noticeId="";
        for (Object object : node) {
            Element elm = (Element)object;  
            accepter=elm.attributeValue("accepter");
            noticeId=elm.attributeValue("noticeId");
            if(StringUtils.isEmpty(accepter)){
            	accepter=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(accepter) || StringUtils.isEmpty(noticeId)){
        	replay.setType(Type.error);
        	log.info("修改通知状态失败:参数错误");
        	return replay;
        }
        boolean tag= NoticeAction.updateState(noticeId, accepter);
    	if(!tag){
    		replay.setType(IQ.Type.error);
    		log.info("修改通知状态失败");
    	}
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
