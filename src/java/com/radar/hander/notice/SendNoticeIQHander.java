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
import com.zyt.web.after.notice.remote.bean.ImCrmNotice;

public class SendNoticeIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.PUSH_NOTICE;
    private static final Logger log = LoggerFactory.getLogger(SendNoticeIQHander.class);
	public SendNoticeIQHander() {
		super("SendNoticeIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		if(log.isDebugEnabled()){
			log.debug("@sunshine:发送通知的xml:",packet.toXML());
		}
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String noticeId="";
        String noticeType="";
        String noticeSubject="";
        String toUserName="";
        String forceNotStore="";
        String appName="";
        String accepterType="";
        for (Object object : node) {
            Element elm = (Element)object;  
            noticeType=elm.attributeValue("noticeType");
            noticeSubject=elm.attributeValue("noticeSubject");
            noticeId=elm.attributeValue("noticeId");
            toUserName=elm.attributeValue("toUserName");
            appName=elm.attributeValue("appName");
            accepterType=elm.attributeValue("accepterType");
            forceNotStore=elm.attributeValue("forceNotStore");
        }
        if(StringUtils.isEmpty(noticeSubject) || StringUtils.isEmpty(noticeType)){
        	replay.setType(Type.error);
        	log.error("@sunshine:发送通知参数错误",packet.toXML());
        	return replay;
        }
        String[] toUserNames=null;
        if(StringUtils.isNotEmpty(toUserName)){
        	toUserNames=toUserName.split("[,]");
        }
        ImCrmNotice imCrmNotice=new ImCrmNotice();
        imCrmNotice.setNoticeId(noticeId);
        imCrmNotice.setSender(packet.getFrom().getNode());
        imCrmNotice.setNoticeType(noticeType);
        imCrmNotice.setNoticeSubject(noticeSubject);
        imCrmNotice.setExtension1(appName);
        imCrmNotice.setExtension2(accepterType);
        boolean force=false;
        if(StringUtils.isNotEmpty(forceNotStore) && ("true".equalsIgnoreCase(forceNotStore) || "1".equals(forceNotStore) || "y".equalsIgnoreCase(forceNotStore))){
        	force=true;
        }
        boolean tag= NoticeAction.sendNotice(imCrmNotice,toUserNames,force);
		if(!tag){
			replay.setType(Type.error);
		}
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
