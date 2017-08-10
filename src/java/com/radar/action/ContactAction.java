package com.radar.action;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mdks.common.pojo.User;
import com.mdks.imcrm.bean.FriendGroups;
import com.mdks.imcrm.bean.Friends;
import com.mdks.imcrm.service.FriendsGroupsRpcService;
import com.mdks.module.user.service.UserRpcService;
import com.radar.common.DubboServer;

public class ContactAction {
	private static final Logger log = LoggerFactory.getLogger(ContactAction.class);
	/**
	 * 获取用户联系人
	 * @Title: friendsList
	 * @Description:   
	 * @param: @param userId
	 * @param: @param userFrom
	 * @param: @return      
	 * @return: Map<FriendGroups,List<Friends>>
	 * @author: sunshine  
	 * @throws
	 */
	public static Map<FriendGroups, List<Friends>> friendsList(String userId,String userFrom){
		try {
			FriendsGroupsRpcService friendsGroupsRpcService = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			Map<FriendGroups, List<Friends>>  map=friendsGroupsRpcService.friendsList(userId,userFrom);
			return map;
		} catch (Exception e) {
			log.error("获取用户联系人异常",e);
		}
		return null;
	}
	/**
	 * 添加分组
	 * @Title: addFriendsGroup
	 * @Description:   
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: FriendGroups
	 * @author: sunshine  
	 * @throws
	 */
	public static FriendGroups addFriendsGroup(FriendGroups imCrmFriendGroups){
		try {
			FriendsGroupsRpcService friendsGroupsRpcService = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			FriendGroups _imCrmFriendGroups=friendsGroupsRpcService.addFriendsGroup(imCrmFriendGroups);
			return _imCrmFriendGroups;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	/**
	 * 添加成员
	 * @Title: addFriendsGroup
	 * @Description:   
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: Friends
	 * @author: sunshine  
	 * @throws
	 */
	public static Friends addMember(Friends imCrmFriends){
		try {
			FriendsGroupsRpcService friendsGroupsRpcService = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			Friends _imCrmFriends=friendsGroupsRpcService.addMember(imCrmFriends);
			return _imCrmFriends;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 移除成员
	 * @Title: delMember
	 * @Description:   
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delMember(Friends imCrmFriends){
		try {
			FriendsGroupsRpcService clent = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			boolean rt=clent.delMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 删除分组
	 * @Title: delFriendsGroup
	 * @Description:   
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean delFriendsGroup(FriendGroups imCrmFriendGroups){
		try {
			FriendsGroupsRpcService clent = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			boolean rt=clent.delFriendsGroup(imCrmFriendGroups);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 修改组名
	 * @Title: updateFriendsGroup
	 * @Description:   
	 * @param: @param imCrmFriendGroups
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean updateFriendsGroup(FriendGroups imCrmFriendGroups){

		try {
			FriendsGroupsRpcService clent = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			boolean rt=clent.updateFriendsGroup(imCrmFriendGroups);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 修改联系人备注
	 * @Title: reNameMember
	 * @Description:   
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean reNameMember(Friends imCrmFriends){
		try {
			FriendsGroupsRpcService clent = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			boolean rt=clent.reNameMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 好友分组转移
	 * @Title: moveMember
	 * @Description:   
	 * @param: @param imCrmFriends
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean moveMember(Friends imCrmFriends){
		try {
			FriendsGroupsRpcService clent = DubboServer.getInstance().getService(FriendsGroupsRpcService.class);
			boolean rt=clent.moveMember(imCrmFriends);
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
    /**
     * 根据用户名登录名称(userName),或者ID查询用户真实名称
     * @Title: queryNickeName
     * @Description:   
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
	 * @Description:   
	 * @param: @param userName
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static User queryUser(String userName) {
		User user=null;
		try {
			UserRpcService clent = DubboServer.getInstance().getService(UserRpcService.class);
			user=clent.findUserByUserName(userName);
			if(user == null){
				user=clent.findUsersById2(userName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
