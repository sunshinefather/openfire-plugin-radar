package com.radar.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.radar.cache.ICacheService;
import com.radar.cache.TestCacheService;
/**
 * IOS设备的deviceToken值的管理：包括deviceToken的保存(更新)、获取、删除
 * @ClassName:  DeviceToken   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:17:09
 */
public class DeviceToken {
	//username做key，deviceToken做值得集合
	private static final String DEVICETOKENS_A = "deviceTokens_a";
	
	//deviceToken做key，username做值得集合
	private static final String DEVICETOKENS_B = "deviceTokens_b";
	
	private static ICacheService cache = new TestCacheService();
	
	private DeviceToken () {}
	
	
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
		} else if(StringUtils.isEmpty(token) && StringUtils.isEmpty(userName)){
			cache.setMap(DEVICETOKENS_A, key, value);
			cache.setMap(DEVICETOKENS_B, value, key);
			
			return;
		}else {
			//此处保证IOS一个设备对应一个username
			//如果一个username可以多出登陆测另外计算
			cache.removeMap(DEVICETOKENS_A, key);
			cache.removeMap(DEVICETOKENS_A, userName);
			cache.removeMap(DEVICETOKENS_B, token);
			cache.removeMap(DEVICETOKENS_B, value);
		}
		
		//其他情况下保存deviceToken
		cache.setMap(DEVICETOKENS_A, key, value);
		cache.setMap(DEVICETOKENS_B, value, key);
	}
	
	/**
	 * 获取deviceToken值
	 * @param key 用户名
	 * @return
	 */
	public static String get(String key){
		if(StringUtils.isEmpty(key)){
			return null;
		}
		
		key = key.trim().toLowerCase();
		
		return cache.get(DEVICETOKENS_A, key, String.class);
	}
	
	/**
	 * 获取所有IOS用户设备deviceToken
	 * @Title: getALL
	 * @Description: TODO  
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static List<String> getAllDeviceTokens(){
		return cache.getMapValue(DEVICETOKENS_A,String.class);
	}
	/**
	 * 获取所有IOS用户设备对应的用户
	 * @Title: getAllUserNames
	 * @Description: TODO  
	 * @param: @return      
	 * @return: List<String>
	 * @author: sunshine  
	 * @throws
	 */
	public static List<String> getAllUserNames(){
		return cache.getMapValue(DEVICETOKENS_B,String.class);
	}
	
	private static String getUserNameByToken(String token){
		if(StringUtils.isEmpty(token)){
			return null;
		}
		return cache.get(DEVICETOKENS_B, token, String.class);
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
		if(!StringUtils.isEmpty(token)){
			cache.removeMap(DEVICETOKENS_B, token);
		}
		
		cache.removeMap(DEVICETOKENS_A, key);
	}
}