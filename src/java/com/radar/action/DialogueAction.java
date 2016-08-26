package com.radar.action;

import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.dialogue.remote.ImCrmDialogueService;

public class DialogueAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
    
	/**
	 * 拉取会话列表
	 * @Title: findDialogueList
	 * @Description: TODO  
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
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmDialogueService.Client.class);
			ImCrmDialogueService.Client clent=(ImCrmDialogueService.Client)clientinfo.getTserviceClient();
			dialogueList=clent.dialogueList(userId,"", pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return dialogueList;
	}
	/**
	 * 删除会话
	 * @Title: delDialogues
	 * @Description: TODO  
	 * @param: @param userId
	 * @param: @param messageIds
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delDialogues(String userId,String dialogueIds,String targetId){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmDialogueService.Client.class);
			ImCrmDialogueService.Client clent=(ImCrmDialogueService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.delDialogue(userId, dialogueIds);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
}
