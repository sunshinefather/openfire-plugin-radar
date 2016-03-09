package com.radar.broadcast.contact;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.xmpp.packet.IQ;

import com.radar.action.GroupAction;
import com.radar.broadcast.SendBoardcastTask;
import com.radar.common.IqConstant;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;
import com.radar.utils.HixinUtils;
/**
 * 发送更新广播队列
 * 主要针对群成员更新和通讯录更新
 * @ClassName:  SendGroupBoradTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年1月28日 下午5:18:44
 */
public class SendGroupBoradTask implements QueueTask {

	private String groupUid;
	private Set<String> items;//个体组广播
	private boolean isGroupUid = false;//群广播

	@Override
	public void executeTask() throws Exception {
		if (isGroupUid) {
			sendGroupBorad(groupUid);
		} else {
			sendGroupBoradJID(items);
		}
	}

	public SendGroupBoradTask(String groupUid) {
		isGroupUid = true;
		this.groupUid = groupUid;
	}

	public SendGroupBoradTask(Set<String> items) {
		this.items = items;
	}

	private void sendGroupBorad(String groupUid) {
		// 更新通讯录的IQ包
		IQ iq = new IQ();
		iq.setFrom(HixinUtils.getDomain());
		iq.setChildElement("query",IqConstant.ALERT_UPDATE_GROUP_MEMBER);

		List<String> items = GroupAction.getGroupMemberListWithUserName(groupUid);
		if (items == null || items.isEmpty()) {
			return;
		}

		for (String jid : items) {
			if (StringUtils.isEmpty(jid)) {
				continue;
			} else {
				ThreadPool.addWork(new SendBoardcastTask(jid, iq));
			}
		}
	}
	private void sendGroupBoradJID(Set<String> items) {
		if (items == null || items.size() == 0) {
			return;
		}

		IQ iq = new IQ();
		iq.setFrom(HixinUtils.getDomain());
		iq.setChildElement("query",IqConstant.ALERT_UPDATE_CONTACT_MEMBER);
		for (String userName : items) {
			ThreadPool.addWork(new SendBoardcastTask(userName, iq));
		}
	}
}
