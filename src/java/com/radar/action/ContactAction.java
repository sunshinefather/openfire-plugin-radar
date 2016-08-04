package com.radar.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.radar.bean.User;
import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.friends.remote.ImCrmFriendGroups;
import com.zyt.web.after.friends.remote.ImCrmFriends;
import com.zyt.web.after.friends.remote.ImCrmFriendsGroupsService;
import com.zyt.web.after.user.remote.ImCrmUserService;

public class ContactAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
	/**
	 * 获取用户联系人
	 * @Title: friendsList
	 * @Description: TODO  
	 * @param: @param userId
	 * @param: @param userFrom
	 * @param: @return      
	 * @return: Map<ImCrmFriendGroups,List<ImCrmFriends>>
	 * @author: sunshine  
	 * @throws
	 */
	public static Map<ImCrmFriendGroups, List<ImCrmFriends>> friendsList(String userId,String userFrom){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			Map<ImCrmFriendGroups, List<ImCrmFriends>>  map=clent.friendsList(userId,userFrom);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	/**
	 * 添加分组
	 * @Title: addFriendsGroup
	 * @Description: TODO  
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: ImCrmFriendGroups
	 * @author: sunshine  
	 * @throws
	 */
	public static ImCrmFriendGroups addFriendsGroup(ImCrmFriendGroups imCrmFriendGroups){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			ImCrmFriendGroups _imCrmFriendGroups=clent.addFriendsGroup(imCrmFriendGroups);
			return _imCrmFriendGroups;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}	
	/**
	 * 添加成员
	 * @Title: addFriendsGroup
	 * @Description: TODO  
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: ImCrmFriends
	 * @author: sunshine  
	 * @throws
	 */
	public static ImCrmFriends addMember(ImCrmFriends imCrmFriends){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			ImCrmFriends _imCrmFriends=clent.addMember(imCrmFriends);
			return _imCrmFriends;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return null;
	}
	/**
	 * 移除成员
	 * @Title: delMember
	 * @Description: TODO  
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delMember(ImCrmFriends imCrmFriends){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.delMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 删除分组
	 * @Title: delFriendsGroup
	 * @Description: TODO  
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delFriendsGroup(ImCrmFriendGroups imCrmFriendGroups){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.delFriendsGroup(imCrmFriendGroups);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 修改组名
	 * @Title: updateFriendsGroup
	 * @Description: TODO  
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean updateFriendsGroup(ImCrmFriendGroups imCrmFriendGroups){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.updateFriendsGroup(imCrmFriendGroups);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 修改联系人备注
	 * @Title: reNameMember
	 * @Description: TODO  
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean reNameMember(ImCrmFriends imCrmFriends){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.reNameMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
	/**
	 * 好友分组转移
	 * @Title: moveMember
	 * @Description: TODO  
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean moveMember(ImCrmFriends imCrmFriends){
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmFriendsGroupsService.Client.class);
			ImCrmFriendsGroupsService.Client clent=(ImCrmFriendsGroupsService.Client)clientinfo.getTserviceClient();
			boolean rt=clent.moveMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return false;
	}
    /**
     * 根据用户名登录名称(userName),或者ID查询用户真实名称
     * @Title: queryNickeName
     * @Description: TODO  
     * @param: @param userName
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
	public static String queryNickeName(String userName) {
		String userFullName="";
		User userEntity=queryUser(userName);
		if(userEntity!=null && userEntity.getExtUser()!=null){
			userFullName = userEntity.getExtUser().getNickName();
			if(StringUtils.isEmpty(userFullName)){
				userFullName = userEntity.getExtUser().getFullName();
			}
			if(StringUtils.isEmpty(userFullName)){
				userFullName = userName;
			}
		}
		return userFullName;
	}
	/**
	 * 根据用户名称或者用户ID查询用户
	 * @Title: queryUser
	 * @Description: TODO  
	 * @param: @param userName
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static User queryUser(String userName) {
		User user=null;
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmUserService.Client.class);
			ImCrmUserService.Client clent=(ImCrmUserService.Client)clientinfo.getTserviceClient();
			String userstring=clent.findUserByName(userName);
			if(StringUtils.isNotEmpty(userstring) && !"[]".equals(userstring)){
				user =JSON.parseObject(userstring,User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return user;
	}
}
