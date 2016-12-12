package com.radar.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radar.broadcast.group.GroupBroadcast;
import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.grouproom.remote.ImCrmGroupMember;
import com.zyt.web.after.grouproom.remote.ImCrmGroupRoom;
import com.zyt.web.after.grouproom.remote.ImCrmGroupRoomService;

public class GroupAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
	private static final Logger log = LoggerFactory.getLogger(GroupAction.class);
    /**
     * 创建群
     * @Title: createGroup
     * @Description: TODO  
     * @param: @param owner
     * @param: @param groupName
     * @param: @return
     * @param: @throws Exception      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static ImCrmGroupRoom createGroupRoom(ImCrmGroupRoom imCrmGroupRoom){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			ImCrmGroupRoom _imCrmGroupRoom=clent.addGroupRoom(imCrmGroupRoom);
			return _imCrmGroupRoom;
		} catch (Exception e) {
			log.info("创建群失败:"+e.getMessage());
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	/**
	 * 修改群信息
	 * @Title: mergeGroupRoom
	 * @Description: TODO  
	 * @param: @param imCrmGroupRoom
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean mergeGroupRoom(ImCrmGroupRoom imCrmGroupRoom){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.mergeGroupRoom(imCrmGroupRoom);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
    /**
     * 添加群成员
     * @Title: addMember
     * @Description: TODO  
     * @param: @param imCrmGroupMember
     * @param: @return      
     * @return: ImCrmGroupMember
     * @author: sunshine  
     * @throws
     */
	public static ImCrmGroupMember addMember(ImCrmGroupMember imCrmGroupMember,String from){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			ImCrmGroupMember _imCrmGroupMember=clent.addMember(imCrmGroupMember);
			if(StringUtils.isNotEmpty(_imCrmGroupMember.getGroupUserId()) && "0".equals(getGroupRoomById(imCrmGroupMember.getGroupId()).getGroupType())){
			  GroupBroadcast.memberJoinBoard(imCrmGroupMember.getUserName(), imCrmGroupMember.getGroupId(),from);
			}else{
				//log.error("@sunshine:添加群成员失败:imcrm=("+imCrmGroupMember.getUserName()+","+imCrmGroupMember.getGroupId()+")");
			}
            return _imCrmGroupMember;
		} catch (Exception e) {
			log.error("@sunshine:添加群成员异常("+imCrmGroupMember.getUserName()+","+imCrmGroupMember.getGroupId()+")",e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	/**移除群成员
	 * 
	 * @Title: delMember
	 * @Description: TODO  
	 * @param: @param imCrmGroupMember
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delMember(ImCrmGroupMember imCrmGroupMember,String from){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.delMember(imCrmGroupMember);
			if(rt && StringUtils.isNotEmpty(imCrmGroupMember.getGroupId()) && "0".equals(getGroupRoomById(imCrmGroupMember.getGroupId()).getGroupType())){
				GroupBroadcast.memberExitBoard(imCrmGroupMember.getUserName(),imCrmGroupMember.getGroupId(),from);
			}
            return rt;
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 获取群列表
	 * @Title: findGroupRoomList
	 * @Description: TODO  
	 * @param: @param userId
	 * @param: @param userFrom
	 * @param: @return      
	 * @return: List<ImCrmGroupRoom>
	 * @author: sunshine  
	 * @throws
	 */
	public static List<ImCrmGroupRoom> findGroupRoomList(String userId,String userFrom){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			List<ImCrmGroupRoom> grouplist=clent.groupRoomList(userId, userFrom);
            return grouplist;
		} catch (Exception e) {
			log.error("@sunshine:获取群列表失败,用户id:"+userId,e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	/**
	 * 根据群id获取群信息
	 * @Title: getGroupRoomById
	 * @Description: TODO  
	 * @param: @param groupId
	 * @param: @return      
	 * @return: ImCrmGroupRoom
	 * @author: sunshine  
	 * @throws
	 */
	public static ImCrmGroupRoom getGroupRoomById(String groupId){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			ImCrmGroupRoom groupRoom=clent.getGroupRoomById(groupId);
            return groupRoom;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return new ImCrmGroupRoom();
	}
	/**
	 * 获取群成员列表
	 * @Title: getGroupMemberList
	 * @Description: TODO  
	 * @param: @param groupId
	 * @param: @return      
	 * @return: List<ImCrmGroupMember>
	 * @author: sunshine  
	 * @throws
	 */
	public static String getGroupMemberList(String groupId,String pageSize,String pageNo){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			String memberlist=clent.getGroupMembers(groupId,pageSize,pageNo);
            return memberlist;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	
	@Deprecated
	public static List<ImCrmGroupMember> getGroupMemberList(String groupId){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			List<ImCrmGroupMember> groupMemberList=clent.getGroupMemberList(groupId);
			return groupMemberList;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	
	public static List<String> getGroupMemberListWithUserName(String groupId){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmGroupRoomService.Client.class);
			ImCrmGroupRoomService.Client clent=(ImCrmGroupRoomService.Client)clientinfo.getTserviceClient();
			List<String> memberlist=clent.getGroupMemberListWithUserName(groupId);
			return memberlist;
		} catch (Exception e) {
			log.error("@sunshine:根据群id获取群成员登陆名失败,群id:"+groupId,e);
		}finally{
			ThriftClientManager.closeClient(clientinfo);
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