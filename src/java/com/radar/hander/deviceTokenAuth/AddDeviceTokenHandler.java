package com.radar.hander.deviceTokenAuth;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.DeviceToken;

/**
 * 保存IOS DeviceToken 值
 *
 */
public class AddDeviceTokenHandler extends IQHandler{

	private IQHandlerInfo info;
	private final static String NAME_SPACE = "jabber:iq:auth:login";
	private static final Logger log = LoggerFactory.getLogger(AddDeviceTokenHandler.class);
    
	public AddDeviceTokenHandler() {
		 super("AddDeviceTokenHandler");
		 info = new IQHandlerInfo("query", NAME_SPACE);
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

	@Override
	public IQ handleIQ(IQ iq) throws UnauthorizedException {
		//创建需要返回IQ包
		IQ reply = IQ.createResultIQ(iq);		
		//获取deviceToken并保存
		try {
			String userName = iq.getChildElement().element("username").getText();
			String deviceToken = iq.getChildElement().element("deviceToken").getText();

			if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(deviceToken)) {
				DeviceToken.put(userName, deviceToken);
			}
		} catch (Exception e) {
			log.error("添加deviceToken失败：", e.getMessage());
			reply.setType(IQ.Type.error);
		}
		
        return reply;
	}
}
