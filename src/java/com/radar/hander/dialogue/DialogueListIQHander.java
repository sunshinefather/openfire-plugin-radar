package com.radar.hander.dialogue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.DialogueAction;
import com.radar.common.IqConstant;

public class DialogueListIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.DIALOGUE_LIST;
    private static final Logger log = LoggerFactory.getLogger(DialogueListIQHander.class);
	public DialogueListIQHander() {
		super("DialogueListIQHander");
		info = new IQHandlerInfo("query", NAME_SPACE);
		
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ replay=IQ.createResultIQ(packet);
		replay.setChildElement("query", NAME_SPACE);
		Element query = packet.getChildElement();
        List<?> node =  query.elements();
        String userId="";
        int pageNo=0;
        int pageSize=10;
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
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
        if(StringUtils.isEmpty(userId)){
        	replay.setType(IQ.Type.error);
        	log.info("获取会话列表参数错误");
        }else {
        	String msglist=DialogueAction.findDialogueList(userId, pageNo, pageSize);
        	if(StringUtils.isEmpty(msglist)){
        		replay.setType(IQ.Type.error);
        		log.info("获取会话列表失败");
        	}else{
            	replay.getChildElement().addElement("dialoguesJson").setText(msglist);
        	}
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
