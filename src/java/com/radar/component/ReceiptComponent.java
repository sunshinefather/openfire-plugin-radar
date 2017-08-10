package com.radar.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import com.radar.cache.TestDBCache;
import com.radar.utils.HixinUtils;
/**
 * 处理客户端收到群消息后的回执信息组件
 * @ClassName:  ReceiptComponent   
 *    
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:28:45
 */
public class ReceiptComponent implements Component {
	private static final Logger log = LoggerFactory.getLogger(ReceiptComponent.class);
	private final static String name = "receiptComponent";

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void initialize(JID jid, ComponentManager componentManager)
			throws ComponentException {

	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			if (packet.getTo() != null && packet.getTo().getDomain().startsWith(HixinUtils.getReceiptName())) {//群消息回执
				Message message = (Message) packet.createCopy();
				String username = message.getFrom().getNode();
				// 删除离线消息
				log.debug("删除离线消息：" + username);
				TestDBCache.removeUserMessage(username);
			}
		}
	}

	@Override
	public void shutdown() {}

	@Override
	public void start() {}
}
