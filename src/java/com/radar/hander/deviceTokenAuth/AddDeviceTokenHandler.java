package com.radar.hander.deviceTokenAuth;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;

import com.radar.action.DeviceToken;
import com.radar.extend.IosTokenDao;
import com.radar.extend.UserInfo;
/**
 * 保存IOS DeviceToken 值
 * @ClassName:  AddDeviceTokenHandler   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年3月16日 上午10:10:40
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
		if(log.isDebugEnabled()){
			log.debug("ios注册token:"+iq.toXML());	
		}
		//创建需要返回IQ包
		IQ replay = IQ.createResultIQ(iq);	
		replay.setChildElement("query", NAME_SPACE);
		//获取deviceToken并保存
		try {
			String userName = iq.getChildElement().element("username").getText();
			String deviceToken = iq.getChildElement().element("deviceToken").getText();
			String appName = iq.getChildElement().element("appName").getText();
			String userType = iq.getChildElement().element("userType").getText();
			String version = iq.getChildElement().element("version").getText();
			if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(version)){
				UserInfo userInfo = new UserInfo();
				userInfo.setUserName(userName);
				userInfo.setIosToken(deviceToken);
				userInfo.setAppName(appName);
				userInfo.setUserType(userType);
				userInfo.setVersion(version);
				IosTokenDao.getInstance().saveUserInfo(userInfo);
			}else if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(deviceToken)) {
				DeviceToken.put(userName, deviceToken);
				
			}
		} catch (Exception e) {
			log.error("添加deviceToken失败：",e);
			replay.setType(IQ.Type.error);
		}
		
        return replay;
	}
}
