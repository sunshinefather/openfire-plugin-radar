package com.radar.interceptor;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.radar.action.DeviceToken;
import com.radar.utils.HixinUtils;
/**
 * 取消绑定ios设备token，由极光维护 ，旧版本保持不变
 * 1.拦截资源绑定保存IOSdeviceToken
 * 2.拦截IOSdeviceToken：移除deviceToken
 * @ClassName:  IQInterceptor
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午3:58:51
 */
public class IQInterceptor implements PacketInterceptor {
	private static final String MOBILE = HixinUtils.getResource();
	private static final Logger log = LoggerFactory.getLogger(IQInterceptor.class);

	@Override
	public void interceptPacket(Packet packet, Session session,
			boolean incoming, boolean processed) throws PacketRejectedException {
		//程序执行中；是否为结束或返回状态（是否是用户发送的消息）
		if(!processed && !incoming && packet instanceof IQ ){
			IQ iq = (IQ) packet;
			if(iq.getType().equals(IQ.Type.result)){
				try{
				if("urn:ietf:params:xml:ns:xmpp-bind".equals(iq.getChildElement().getNamespace().getStringValue())){
					String jidstr=iq.getChildElement().node(0).getText();
					if(jidstr!=null){
						JID jid=new JID(jidstr);
						if(StringUtils.isNotEmpty(jid.getResource()) && jid.getResource().length()>=32){
							DeviceToken.put(jid.getNode(), jid.getResource());
						}
					}
				}
				}catch(Exception e){		
				}
			}
		}
		if (processed || !incoming) {
			return;
		}

		// 过滤分发后的群消息
		if (packet.getFrom() != null && StringUtils.isNotEmpty(packet.getFrom().getDomain()) && packet.getFrom().getDomain().startsWith(HixinUtils.getGroupPrefix())) {
			return;
		}

		// 登录时判断是不是IOS设备登录，如果为IOS设备登录则保存deviceToken值，如果不是则删除deviceToken值
		if (packet instanceof IQ) {
			IQ iq = (IQ) packet;
			if (iq.getChildElement() == null) {
				return;
			}

			String nameSpace = iq.getChildElement().getNamespaceURI();
			if ("jabber:iq:auth".equals(nameSpace)) {
				try {
					String userName = iq.getChildElement().element("username").getText();
					if (iq.getChildElement().element("resource") == null) {
						return;
					}
					String device = iq.getChildElement().element("resource").getText();
					if (MOBILE.equals(device)) {
						DeviceToken.remove(userName);
					}
				} catch (Exception e) {
					log.info("Get deviceToken exception:" + e.getMessage());
					e.printStackTrace();
				}
			} else if ("urn:xmpp:ping".equals(nameSpace)) {
				try {
					ClientSession clientSession = XMPPServer.getInstance().getSessionManager().getSession(packet.getFrom());

					//收到ping包时，发现presence Type为unavailable 则说明：
					//session连接存在,Presence状态不对，服务器设置为available
					if (clientSession == null || clientSession.getPresence().getType() == Presence.Type.unavailable) {
						Presence presence = new Presence();
						presence.setFrom(packet.getFrom());
						XMPPServer.getInstance().getPresenceUpdateHandler().process(presence);
					}
				} catch (Exception e) {
					log.info("send ping available exception:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}