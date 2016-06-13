package com.radar.action;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radar.broadcast.notice.NoticeBroadcast;
import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.notice.remote.bean.ImCrmNotice;
import com.zyt.web.after.notice.remote.bean.ImCrmNoticeVO;
import com.zyt.web.after.notice.remote.service.ImCrmNoticeService;

public class NoticeAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
    private static final Logger log = LoggerFactory.getLogger(NoticeAction.class);
	
	/**
	 * 发送通知
	 * @Title: sendNotice
	 * @Description: TODO  
	 * @param: @param imCrmNotice
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean sendNotice(ImCrmNotice imCrmNotice,String[] toUserNames,Boolean... forceNotStore){
		NoticeBroadcast.pushNotice(imCrmNotice,toUserNames,forceNotStore);
		boolean tag=true;
		if(toUserNames!=null && toUserNames.length>0 && !(forceNotStore!=null && forceNotStore.length>0 && forceNotStore[0])){
			ThriftClientInfo clientinfo=null;
			try {
				clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
				ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
				if(StringUtils.isNotEmpty(clent.send(imCrmNotice).getNoticeId())){
					tag=true;
				}else{
					tag=false;
				}
			} catch (Exception e) {
				tag=false;
				log.error("保存通知异常",e);
			}finally{
				ThriftClientManager.closeClient(clientinfo);
			}
		}
		return tag;
	}
	/**
	 * 修改通知状态
	 * @Title: updatestate
	 * @Description: TODO  
	 * @param: @param noticeId
	 * @param: @param accepter
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean updateState(String noticeId, String accepter){
		boolean tag=false;
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
			ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
			tag=clent.updateState(noticeId, accepter);
		} catch (Exception e) {
			log.error("修改通知 状态异常",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return tag;
	}
	/**
	 * 拉取接收通知列表
	 * @Title: findAccepterList
	 * @Description: TODO  
	 * @param: @param pageModel
	 * @param: @return      
	 * @return: List<ImCrmNotice>
	 * @author: sunshine  
	 * @throws
	 */
	public static String findAccepterList(String accepter, String noticeType, int pageNo, int pageSize){
		String noticeList="";
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
			ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
			noticeList=clent.findAccepterList(accepter, noticeType, pageNo, pageSize);
		} catch (Exception e) {
			log.error("获取接収通知异常",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return noticeList;
	}
	/**
	 * 拉取发送通知列表
	 * @Title: findSenderList
	 * @Description: TODO  
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
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
			ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
			noticeList=clent.findSenderList(sender, noticeType, pageNo, pageSize);
		} catch (Exception e) {
			log.error("获取发送通知列表异常",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return noticeList;
	}
	
	/**
	 * 获取未读通知
	 * @Title: findUnread
	 * @Description: TODO  
	 * @param: @param accepter
	 * @param: @param noticeType
	 * @param: @return      
	 * @return: ImCrmNoticeVO
	 * @author: sunshine  
	 * @throws
	 */
	public static ImCrmNoticeVO findUnread(String accepter, String noticeType){
		ImCrmNoticeVO imCrmNoticeVO=null;
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
			ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
			imCrmNoticeVO=clent.findUnread(accepter, noticeType);
		} catch (Exception e) {
			log.error("获取未读通知异常",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return imCrmNoticeVO;
	}
	/**
	 * 获取通知类容以及详细
	 * @Title: findById
	 * @Description: TODO  
	 * @param: @param accepter
	 * @param: @param noticeId
	 * @param: @return      
	 * @return: ImCrmNoticeVO
	 * @author: sunshine  
	 * @throws
	 */
	public static ImCrmNotice findById(String userName, String noticeId){
		ImCrmNotice notice=null;
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmNoticeService.Client.class);
			ImCrmNoticeService.Client clent=(ImCrmNoticeService.Client)clientinfo.getTserviceClient();
			notice=clent.findById(noticeId);
		} catch (Exception e) {
			log.error("获取通知内容详情异常",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return notice;
	}
}
