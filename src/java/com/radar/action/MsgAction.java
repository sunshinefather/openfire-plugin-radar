package com.radar.action;

import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.messages.remote.ImCrmMessage;
import com.zyt.web.after.messages.remote.ImCrmMessageService;

public class MsgAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
    /**
     * 保存消息
     * @Title: saveMessage
     * @Description: TODO  
     * @param: @param imCrmMessage
     * @param: @return      
     * @return: boolean
     * @author: sunshine  
     * @throws
     */
	public static boolean saveMessage(ImCrmMessage imCrmMessage){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmMessageService.Client.class);
			ImCrmMessageService.Client clent=(ImCrmMessageService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.saveMessage(imCrmMessage);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 修改消息读取状态
	 * @Title: updateMessageState
	 * @Description: TODO  
	 * @param: @param messageId
	 * @param: @param accepter
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean updateMessageState(String messageId,String accepter){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmMessageService.Client.class);
			ImCrmMessageService.Client clent=(ImCrmMessageService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.updateMessageState(messageId, accepter);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 获取聊天记录
	 * @Title: findMessageList
	 * @Description: TODO  
	 * @param: @param userId
	 * @param: @param targetId
	 * @param: @param pageNo
	 * @param: @param pageSize
	 * @param: @param chatType 1:单聊;2:群聊
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String findMessageList(String userId,String targetId,int pageNo,int pageSize,String chatType){
		ThriftClientInfo clientinfo=null;
		String msglist="";
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmMessageService.Client.class);
			ImCrmMessageService.Client clent=(ImCrmMessageService.Client)clientinfo.getTserviceClient();
			msglist=clent.findMessageList(userId, targetId, pageNo, pageSize, chatType);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return msglist;
	}
	/**
	 * 消息撤回(删除聊天记录)
	 * @Title: delMessageState
	 * @Description: TODO  
	 * @param: @param userId
	 * @param: @param messageIds
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delMessage(String userId,String messageIds){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmMessageService.Client.class);
			ImCrmMessageService.Client clent=(ImCrmMessageService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.delMessages(userId, messageIds);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
}
