package com.radar.action;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mdks.imcrm.bean.GroupMember;
import com.mdks.imcrm.bean.GroupRoom;
import com.mdks.imcrm.service.GroupRoomRpcService;
import com.radar.broadcast.group.GroupBroadcast;
import com.radar.common.DubboServer;

public class GroupAction {
	private static final Logger log = LoggerFactory.getLogger(GroupAction.class);
    /**
     * 创建群
     * @Title: createGroup
     * @Description:   
     * @param: @param owner
     * @param: @param groupName
     * @param: @return
     * @param: @throws Exception      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static GroupRoom createGroupRoom(GroupRoom imCrmGroupRoom){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			GroupRoom _imCrmGroupRoom=clent.addGroupRoom(imCrmGroupRoom);
			return _imCrmGroupRoom;
		} catch (Exception e) {
			log.info("创建群失败:"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 修改群信息
	 * @Title: mergeGroupRoom
	 * @Description:   
	 * @param: @param imCrmGroupRoom
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean mergeGroupRoom(GroupRoom imCrmGroupRoom){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			boolean rt=clent.mergeGroupRoom(imCrmGroupRoom);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
    /**
     * 添加群成员
     * @Title: addMember
     * @Description:   
     * @param: @param imCrmGroupMember
     * @param: @return      
     * @return: GroupMember
     * @author: sunshine  
     * @throws
     */
	public static GroupMember addMember(GroupMember imCrmGroupMember,String from){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			GroupMember _imCrmGroupMember=clent.addMember(imCrmGroupMember);
			if(StringUtils.isNotEmpty(_imCrmGroupMember.getGroupUserId()) && "0".equals(getGroupRoomById(imCrmGroupMember.getGroupId()).getGroupType())){
			  GroupBroadcast.memberJoinBoard(imCrmGroupMember.getUserName(), imCrmGroupMember.getGroupId(),from);
			}
            return _imCrmGroupMember;
		} catch (Exception e) {
			log.error("@sunshine:添加群成员异常("+imCrmGroupMember.getUserName()+","+imCrmGroupMember.getGroupId()+")",e);
		}
		return null;
	}
	/**移除群成员
	 * 
	 * @Title: delMember
	 * @Description:   
	 * @param: @param imCrmGroupMember
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delMember(GroupMember imCrmGroupMember,String from){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			boolean rt=clent.delMember(imCrmGroupMember);
			if(rt && StringUtils.isNotEmpty(imCrmGroupMember.getGroupId()) && "0".equals(getGroupRoomById(imCrmGroupMember.getGroupId()).getGroupType())){
				GroupBroadcast.memberExitBoard(imCrmGroupMember.getUserName(),imCrmGroupMember.getGroupId(),from);
			}
            return rt;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	/**
	 * 获取群列表
	 * @Title: findGroupRoomList
	 * @Description:   
	 * @param: @param userId
	 * @param: @param userFrom
	 * @param: @return      
	 * @return: List<GroupRoom>
	 * @author: sunshine  
	 * @throws
	 */
	public static List<GroupRoom> findGroupRoomList(String userId,String userFrom){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			List<GroupRoom> grouplist=clent.groupRoomList(userId, userFrom);
            return grouplist;
		} catch (Exception e) {
			log.error("@sunshine:获取群列表失败,用户id:"+userId,e);
		}
		return null;
	}
	/**
	 * 根据群id获取群信息
	 * @Title: getGroupRoomById
	 * @Description:   
	 * @param: @param groupId
	 * @param: @return      
	 * @return: GroupRoom
	 * @author: sunshine  
	 * @throws
	 */
	public static GroupRoom getGroupRoomById(String groupId){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			GroupRoom groupRoom=clent.getGroupRoomById(groupId);
            return groupRoom;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GroupRoom();
	}
	/**
	 * 获取群成员列表
	 * @Title: getGroupMemberList
	 * @Description:   
	 * @param: @param groupId
	 * @param: @return      
	 * @return: List<GroupMember>
	 * @author: sunshine  
	 * @throws
	 */
	public static String getGroupMemberList(String groupId,String pageSize,String pageNo){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			String memberlist=clent.getGroupMembers(groupId,pageSize,pageNo);
            return memberlist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated
	public static List<GroupMember> getGroupMemberList(String groupId){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			List<GroupMember> groupMemberList=clent.getGroupMemberList(groupId);
			return groupMemberList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getGroupMemberListWithUserName(String groupId){
		try {
			GroupRoomRpcService clent = DubboServer.getInstance().getService(GroupRoomRpcService.class);
			List<String> memberlist=clent.getGroupMemberListWithUserName(groupId);
			return memberlist;
		} catch (Exception e) {
			log.error("@sunshine:根据群id获取群成员登陆名失败,群id:"+groupId,e);
		}
		return null;
	}

    /**
     * 根据群ID查询群名称
     * @Title: queryGroupName
     * @param: @param groupUid
     * @return: String
     * @author: sunshine  
     */
	public static String queryGroupName(String groupUid) {
        try{
        	groupUid=getGroupRoomById(groupUid).getGroupName();
        }catch(Exception e){
        	log.error("@sunshine:查询群名称失败,群id:"+groupUid,e);
        }
		return groupUid;
	}
}