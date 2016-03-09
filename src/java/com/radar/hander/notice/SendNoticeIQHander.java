package com.radar.hander.notice;

import java.util.ArrayList;
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
import com.zyt.web.after.notice.remote.bean.ImCrmNoticeDetail;

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
		IQ replay=IQ.createResultIQ(packet);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String noticeId="";
        String noticeType="";
        String noticeSubject="";
        String toUserName="";
        String noticeContent="";
        String forceNotStore="";
        for (Object object : node) {
            Element elm = (Element)object;  
            noticeId=elm.attributeValue("noticeId");
            noticeType=elm.attributeValue("noticeType");
            noticeSubject=elm.attributeValue("noticeSubject");
            noticeContent=elm.attributeValue("noticeContent");
            toUserName=elm.attributeValue("toUserName");
            forceNotStore=elm.attributeValue("forceNotStore");
        }
        if(StringUtils.isEmpty(noticeSubject) || StringUtils.isEmpty(noticeType) || (StringUtils.isEmpty(toUserName) && StringUtils.isEmpty(noticeId)) || (StringUtils.isEmpty(noticeContent) && StringUtils.isEmpty(noticeId))){
        	replay.setType(Type.error);
        	log.info("推送失败:参数错误");
        	return replay;
        }
        String[] toUserNames=null;
        if(StringUtils.isNotEmpty(toUserName)){
        	toUserNames=toUserName.split(",");
        }
        ImCrmNotice imCrmNotice=new ImCrmNotice();
        imCrmNotice.setNoticeId(noticeId);
        imCrmNotice.setSender(packet.getFrom().getNode());
        imCrmNotice.setNoticeType(noticeType);
        imCrmNotice.setNoticeSubject(noticeSubject);
        imCrmNotice.setNoticeContent(noticeContent);
        List<ImCrmNoticeDetail> details=new ArrayList<ImCrmNoticeDetail>();
        if(toUserNames!=null && toUserNames.length>0){
            for(String username:toUserNames){
            	ImCrmNoticeDetail e=new ImCrmNoticeDetail();
            	e.setAccepter(username);
            	details.add(e);
            }
        }
        imCrmNotice.setDetails(details);
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
