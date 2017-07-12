package com.radar.action;

import com.mdks.imcrm.bean.Messages;
import com.mdks.imcrm.service.MessageRpcService;
import com.radar.common.DubboServer;

public class MsgAction {
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
	public static boolean saveMessage(Messages imCrmMessage){
		try {
			MessageRpcService clent = DubboServer.getInstance().getService(MessageRpcService.class);
			boolean rt=clent.saveMessage(imCrmMessage);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			MessageRpcService clent = DubboServer.getInstance().getService(MessageRpcService.class);
			boolean rt=clent.updateMessageState(messageId, accepter);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
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
		String msglist="";
		try {
			MessageRpcService clent = DubboServer.getInstance().getService(MessageRpcService.class);
			msglist=clent.findMessageList(userId, targetId, pageNo, pageSize, chatType);
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			MessageRpcService clent = DubboServer.getInstance().getService(MessageRpcService.class);
			boolean rt=clent.delMessages(userId, messageIds);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
