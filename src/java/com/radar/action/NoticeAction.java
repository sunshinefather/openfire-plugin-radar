package com.radar.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mdks.imcrm.bean.Notice;
import com.mdks.imcrm.bean.NoticeVO;
import com.mdks.imcrm.service.NoticeRpcService;
import com.radar.broadcast.notice.NoticeBroadcast;
import com.radar.common.DubboServer;

public class NoticeAction {
    private static final Logger log = LoggerFactory.getLogger(NoticeAction.class);
	
	/**
	 * 发送通知
	 * @Title: sendNotice
	 * @Description:   
	 * @param: @param imCrmNotice
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean sendNotice(Notice imCrmNotice,String[] toUserNames,Boolean... forceNotStore){
		NoticeBroadcast.pushNotice(imCrmNotice,toUserNames,forceNotStore);
		return true;
	}
	/**
	 * 修改通知状态
	 * @Title: updatestate
	 * @Description:   
	 * @param: @param noticeId
	 * @param: @param accepter
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean updateState(String noticeId, String accepter){
		boolean tag=false;
		try {
			NoticeRpcService clent = DubboServer.getInstance().getService(NoticeRpcService.class);
			tag=clent.updateState(noticeId, accepter);
		} catch (Exception e) {
			log.error("修改通知 状态异常",e);
		}
		return tag;
	}
	/**
	 * 拉取接收通知列表
	 * @Title: findAccepterList
	 * @Description:   
	 * @param: @param pageModel
	 * @param: @return      
	 * @return: List<Notice>
	 * @author: sunshine  
	 * @throws
	 */
	public static String findAccepterList(String accepter, String noticeType, int pageNo, int pageSize){
		String noticeList="";
		try {
			NoticeRpcService clent = DubboServer.getInstance().getService(NoticeRpcService.class);
			noticeList=clent.findAccepterList(accepter, noticeType, pageNo, pageSize);
		} catch (Exception e) {
			log.error("获取接収通知异常",e);
		}
		return noticeList;
	}
	/**
	 * 拉取发送通知列表
	 * @Title: findSenderList
	 * @Description:   
	 * @param: @param sender
	 * @param: @param noticeType
	 * @param: @param pageNo
	 * @param: @param pageSize
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String findSenderList(String sender, String noticeType, int pageNo, int pageSize){
		String noticeList="";
		try {
			NoticeRpcService clent = DubboServer.getInstance().getService(NoticeRpcService.class);
			noticeList=clent.findSenderList(sender, noticeType, pageNo, pageSize);
		} catch (Exception e) {
			log.error("获取发送通知列表异常",e);
		}
		return noticeList;
	}
	
	/**
	 * 获取未读通知
	 * @Title: findUnread
	 * @Description:   
	 * @param: @param accepter
	 * @param: @param noticeType
	 * @param: @return      
	 * @return: NoticeVO
	 * @author: sunshine  
	 * @throws
	 */
	public static NoticeVO findUnread(String accepter, String noticeType){
		NoticeVO imCrmNoticeVO=null;
		try {
			NoticeRpcService clent = DubboServer.getInstance().getService(NoticeRpcService.class);
			imCrmNoticeVO=clent.findUnread(accepter, noticeType);
		} catch (Exception e) {
			log.error("获取未读通知异常",e);
		}
		return imCrmNoticeVO;
	}
	/**
	 * 获取通知类容以及详细
	 * @Title: findById
	 * @Description:   
	 * @param: @param accepter
	 * @param: @param noticeId
	 * @param: @return      
	 * @return: NoticeVO
	 * @author: sunshine  
	 * @throws
	 */
	public static Notice findById(String userName, String noticeId){
		Notice notice=null;
		try {
			NoticeRpcService clent = DubboServer.getInstance().getService(NoticeRpcService.class);
			notice=clent.findById(noticeId);
		} catch (Exception e) {
			log.error("获取通知内容详情异常",e);
		}
		return notice;
	}
}
