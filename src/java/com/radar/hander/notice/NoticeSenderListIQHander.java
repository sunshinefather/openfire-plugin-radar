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

public class NoticeSenderListIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.NOTICE_SENDER_LIST;
    private static final Logger log = LoggerFactory.getLogger(NoticeSenderListIQHander.class);
	public NoticeSenderListIQHander() {
		super("NoticeSenderListIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String sender="";
        String noticeType="";
        int pageNo=0;
        int pageSize = 10;
        for (Object object : node) {
            Element elm = (Element)object;  
            sender=elm.attributeValue("sender");
            noticeType=elm.attributeValue("noticeType");
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
            if(StringUtils.isEmpty(sender)){
            	sender=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(sender) || StringUtils.isEmpty(noticeType)){
        	replay.setType(Type.error);
        	log.info("拉取通知列表失败:参数错误");
        	return replay;
        }
        String notice= NoticeAction.findSenderList(sender, noticeType, pageNo, pageSize);
    	if(StringUtils.isEmpty(notice)){
    		replay.setType(IQ.Type.error);
    		log.info("拉取通知列表失败");
    	}else{
        	replay.getChildElement().addElement("noticeJson").setText(notice);
    	}
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
