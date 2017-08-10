package com.radar.action;

import com.mdks.imcrm.service.DialogueRpcService;
import com.radar.common.DubboServer;

public class DialogueAction {
    
	/**
	 * 拉取会话列表
	 * @Title: findDialogueList
	 * @Description:   
	 * @param: @param userId
	 * @param: @param pageNo
	 * @param: @param pageSize
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String findDialogueList(String userId,int pageNo,int pageSize){
		String dialogueList="";
		try {
			DialogueRpcService clent = DubboServer.getInstance().getService(DialogueRpcService.class);
			dialogueList=clent.dialogueList(userId,"", pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialogueList;
	}
	/**
	 * 删除会话
	 * @Title: delDialogues
	 * @Description:   
	 * @param: @param userId
	 * @param: @param messageIds
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delDialogues(String userId,String dialogueIds,String targetId){
		try {
			DialogueRpcService clent = DubboServer.getInstance().getService(DialogueRpcService.class);
			boolean rt=clent.delDialogue(userId, dialogueIds);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
