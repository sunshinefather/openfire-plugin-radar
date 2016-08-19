package com.radar.broadcast.group;

import org.apache.commons.lang.StringUtils;

import com.radar.action.ContactAction;
import com.radar.pool.ThreadPool;
/**
 * 发送群通知信息工具类
 * 群添加成员通知,群成员退出通知 ,群改名通知
 * @ClassName:  GroupBroadcast   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:39:35
 */
public class GroupBroadcast {
	/**
	 * 加入群通知
	 * 
	 * @param addUsersName  加入群的用户名称多个用户用","分割;用[]包含用户
	 * @param groupUid 群id
	 * @param from 发送人
	 */
	public static void memberJoinBoard(String addUsersName, String groupUid,String from) {
		if(StringUtils.isEmpty(addUsersName) || StringUtils.isEmpty(groupUid)){
			return;
		}
		String sendUserName = ContactAction.queryNickeName(from);
		String[] addUsersNames=addUsersName.split(",");
		StringBuffer sb=new StringBuffer();
		int lent=addUsersNames.length;
		if(lent>0){
			int j=1;
			for(String username:addUsersNames){
				String acceptUserName=ContactAction.queryNickeName(username);
				if(StringUtils.isNotEmpty(acceptUserName)){
					sb.append(acceptUserName);
					if(j++<lent){
						sb.append(",");
					}
				}
			}
			if(StringUtils.isNotEmpty(sendUserName) && StringUtils.isNotEmpty(sb.toString())){
				//ThreadPool.addWork(new GroupBroadcastTask("《"+sendUserName+ "》邀请 《" + sb.toString() + "》加入该群组",groupUid));
			}
		}
	}
	
	/**
	 * 退出群 通知
	 * 
	 * @param exitUsersName 用户列表
	 * @param groupUid 群id
	 * @param from　发送人
	 */
	public static void memberExitBoard(String exitUsersName, String groupUid,
			String from) {
		if(StringUtils.isNotEmpty(exitUsersName)){
			ThreadPool.addWork(new GroupBroadcastTask("您已被移出该群",groupUid,exitUsersName));
		}
	}

	/**
	 * 修改群组名称
	 * 
	 * @param groupUid 群UID
	 * @param from　发送人
	 */
	public static void groupRenameBoard(String groupName, String groupUid,
			String from) {
		//ThreadPool.addWork(new GroupBroadcastTask("《"+ContactAction.queryNickeName(from) + "》将该群组名称修改为：" + groupName, groupUid));
	}
}