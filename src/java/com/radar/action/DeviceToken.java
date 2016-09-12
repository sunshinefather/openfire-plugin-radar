package com.radar.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.radar.extend.IosTokenDao;
import com.radar.extend.PaginationAble;

/**
 * IOS设备的deviceToken值的管理：包括deviceToken的保存(更新)、获取、删除
 * @ClassName:  DeviceToken   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:17:09
 */
public class DeviceToken {
	private DeviceToken () {
		throw new IllegalAccessError("访问异常");
	}
	
	
	/**
	 * 添加deviceToken值
	 * @param key 用户名
	 * @param value deviceToken值
	 */
	public static void put(String key, String value){
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
			return;
		}
		
		key = key.trim().toLowerCase();
		value = value.trim().toLowerCase();
		
		String token = get(key);
		String userName = getUserNameByToken(value);
		
		if (value.equals(token) && key.equals(userName)) {
			//deviceToken不为空的情况下，deviceToken等于当前的值，直接返回
			return;
		} else if(StringUtils.isEmpty(token) && StringUtils.isEmpty(userName)){//新加用户
				IosTokenDao.getInstance().saveIosToken(key, value);
			return;
		}else {
			//此处保证IOS一个设备对应一个username
			//如果一个username可以多出登陆测另外计算
			IosTokenDao.getInstance().delTokenByUser(key);
			IosTokenDao.getInstance().delUserByToken(value);
			if(StringUtils.isNotEmpty(token)){
				IosTokenDao.getInstance().delUserByToken(token);
			}
			if(StringUtils.isNotEmpty(userName)){
				IosTokenDao.getInstance().delTokenByUser(userName);
			}

		}
		//其他情况下保存deviceToken
		IosTokenDao.getInstance().saveIosToken(key, value);

	}
	
	/**
	 * 获取deviceToken值
	 * @param key 用户名
	 * @return
	 */
	public static String get(String key,boolean... isgetVersion){
		if(StringUtils.isEmpty(key)){
			return null;
		}
		key = key.trim().toLowerCase();
		String token = IosTokenDao.getInstance().getTokenByUserName(key);
		/*
		if(isgetVersion==null || isgetVersion.length==0){
			if(StringUtils.isNotEmpty(token)){
				token=token.split("[,]")[0];
			}
		}
		*/
		return token;
	}
	
	/**
	 * 获取所有IOS用户设备deviceToken(数据量小可以使用)
	 * @Title: getALL
	 * @Description: TODO  
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	@Deprecated
	public static List<String> getAllDeviceTokens(){
		List<String> list =new ArrayList<String>();
		list.addAll(IosTokenDao.getInstance().getAllUser().keySet());
		return list;
	}
	
	/**
	 * 分页获取设备
	 * @Title: getDeviceTokensByPage
	 * @Description: TODO  
	 * @param: @return      
	 * @return: List<String>
	 * @author: sunshine  
	 * @throws
	 */
	public static PaginationAble getDeviceTokensByPage(PaginationAble page){
		page.setTotalResults(IosTokenDao.getInstance().getTotal());
		return IosTokenDao.getInstance().getTokenByPage(page);
	}
	/**
	 * 获取所有IOS用户设备对应的用户(数据量小可以使用)
	 * @Title: getAllUserNames
	 * @Description: TODO  
	 * @param: @return      
	 * @return: List<String>
	 * @author: sunshine  
	 * @throws
	 */
	@Deprecated
	public static List<String> getAllUserNames(){
		List<String> list =new ArrayList<String>();
		 list.addAll(IosTokenDao.getInstance().getAllUser().values());
		return list;
	}

	public static String getUserNameByToken(String token){
		if(StringUtils.isEmpty(token)){
			return null;
		}
		
		token = token.trim();
	 return IosTokenDao.getInstance().getUserNameByToken(token);
	}
	
	/**
	 * 删除用户对应的deviceToken值
	 * @param key 用户名
	 */
	public static void remove(String key){
		if(StringUtils.isEmpty(key)){
			return ;
		}
		
		key = key.trim().toLowerCase();
		String token = get(key);
		if(StringUtils.isNotEmpty(token)){
			IosTokenDao.getInstance().delUserByToken(token);
		}
		IosTokenDao.getInstance().delTokenByUser(key);
	}
}