package com.radar.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mdks.imcrm.service.DialogueRpcService;
import com.radar.common.DubboServer;

public class DialogueAction {
	private static final Logger log = LoggerFactory.getLogger(DialogueAction.class);
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
			log.error("@sunshine:拉取会话列表异常",e);
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
			log.error("@sunshine:删除会话异常",e);
		}
		return false;
	}
}
