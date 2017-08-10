package com.radar.broadcast.notice;

import org.apache.commons.lang.StringUtils;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;
import com.mdks.imcrm.bean.Notice;
import com.radar.broadcast.SendBoardcastTask;
import com.radar.pool.ThreadPool;
import com.radar.utils.HixinUtils;
/**
 * 发送系统通知工具类
 * @ClassName:  NoticeBroadcast   
 *    
 * @author: sunshine  
 * @date:   2015年4月17日 下午5:36:13
 */
public class NoticeBroadcast {

	public static void pushNotice(Notice imCrmNotice,String[] toUserNames,Boolean... forceNotStore) {
		Message msg=new Message();
		msg.setType(Message.Type.headline);
		msg.setSubject(imCrmNotice.getNoticeSubject());
		msg.setFrom(new JID(imCrmNotice.getSender()+"@"+HixinUtils.getDomain()));
		PacketExtension pkg=new PacketExtension("notice", "notice:extension:type");
		pkg.getElement().addElement("type").setText(imCrmNotice.getNoticeType());
		msg.addExtension(pkg);
		msg.setBody(imCrmNotice.getNoticeId());
		if(StringUtils.isNotEmpty(imCrmNotice.getNoticeId())){
			msg.setID(imCrmNotice.getNoticeId());
		}
	    if(toUserNames!=null && toUserNames.length>0){
			for(String username:toUserNames){
				if(StringUtils.isNotEmpty(username)){
					ThreadPool.addWork(new SendBoardcastTask(username,msg,forceNotStore));
				}
			}
	    }else{
	    	ThreadPool.addWork(new SendBoardcastTask(imCrmNotice.getExtension1(),imCrmNotice.getExtension2(),msg,forceNotStore));
	    }
	}
}