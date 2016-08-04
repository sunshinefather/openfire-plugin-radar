package com.radar.hander.deviceTokenAuth;

import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.DeviceToken;
/**
 * 正常退出时，删除IOS DeviceToken 值
 * @ClassName:  RemoveDeviceTokenHandler   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年3月16日 上午10:10:28
 */
public class RemoveDeviceTokenHandler extends IQHandler {
	
	private IQHandlerInfo info;
	private final static String NAME_SPACE = "jabber:iq:auth:logout";
	private static final Logger log = LoggerFactory.getLogger(RemoveDeviceTokenHandler.class);
    
	public RemoveDeviceTokenHandler() {
		 super("RemoveDeviceTokenHandler");
		 info = new IQHandlerInfo("query", NAME_SPACE);
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

	@Override
	public IQ handleIQ(IQ iq) throws UnauthorizedException {
		IQ replay = IQ.createResultIQ(iq);
		replay.setChildElement("query", NAME_SPACE);
		try {
			String userName = iq.getChildElement().element("username").getText();
			DeviceToken.remove(userName);
		} catch (Exception e) {
			log.error("删除DeviceToken失败", e.getMessage());
			replay.setType(IQ.Type.error);
		}
        return replay;
	}
}