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

public class DialogueDelIQHander extends IQHandler {

	private IQHandlerInfo info;
	private final static String NAME_SPACE = IqConstant.DEL_DIALOGUE_IDS;
    private static final Logger log = LoggerFactory.getLogger(DialogueDelIQHander.class);
	public DialogueDelIQHander() {
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
        String dialogueIds="";
        for (Object object : node) {
            Element elm = (Element)object;  
            userId=elm.attributeValue("userId");
            dialogueIds=elm.attributeValue("dialogueIds");
            if(StringUtils.isEmpty(userId)){
            	userId=packet.getFrom().getNode();
            }
        }
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(dialogueIds)){
        	replay.setType(IQ.Type.error);
        	log.info("删除会话参数错误");
        }else {
        	boolean bool=DialogueAction.delDialogues(userId, dialogueIds);
        	if(bool){
            	replay.getChildElement().addElement("dialogueIds").setText(dialogueIds);
        	}else{
            	replay.setType(IQ.Type.error);
            	log.info("删除会话失败");
        	}
        }
		return replay;
	}

	@Override
	public IQHandlerInfo getInfo() {
		
		return info;
	}

}
