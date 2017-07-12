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
import com.mdks.imcrm.bean.Notice;
import com.mdks.imcrm.bean.NoticeDetial;
import com.radar.action.NoticeAction;
import com.radar.common.IqConstant;

public class NoticeByIdIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.GET_NOTICE_BYID;
    private static final Logger log = LoggerFactory.getLogger(NoticeByIdIQHander.class);
	public NoticeByIdIQHander() {
		super("NoticeByIdIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        String noticeId="";
        String allUserState="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            noticeId=elm.attributeValue("noticeId");
            allUserState=elm.attributeValue("allUserState");
            if(StringUtils.isEmpty(userId)){
            	userId=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(noticeId)){
        	replay.setType(Type.error);
        	log.info("获取通知失败:参数错误");
        	return replay;
        }
        Notice notice= NoticeAction.findById(userId, noticeId);
    	if(notice!=null && StringUtils.isEmpty(notice.getNoticeId())){
    		replay.setType(IQ.Type.error);
    		log.info("获取通知失败");
    	}else{
        	Element element= replay.getChildElement();
        	Element noticeNode=element.addElement("notice")
        			                  .addAttribute("noticeId", notice.getNoticeId())
        			                  .addAttribute("sender", notice.getSender())
        	                          .addAttribute("noticeType", notice.getNoticeType())
        	                          .addAttribute("noticeSubject", notice.getNoticeSubject())
        	                          .addAttribute("noticeContent", notice.getNoticeContent())
        	                          .addAttribute("sendTime", notice.getExtension1())
        	                          .addAttribute("attachment", notice.getAttachment());
        	if("1".equals(allUserState)){
            	List<NoticeDetial> detailList= notice.getDetails();
            	for(NoticeDetial detail:detailList){
                	noticeNode.addElement("accepter").addAttribute("userName",detail.getAccepter())
                	                                 .addAttribute("readState", detail.getReadState())
                	                                 .addAttribute("readTime", detail.getReadTime())
                	                                 .addAttribute("extension1", detail.getExtension1())
                	                                 .addAttribute("extension2", detail.getExtension2());
            	}
        	}
    	}
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
