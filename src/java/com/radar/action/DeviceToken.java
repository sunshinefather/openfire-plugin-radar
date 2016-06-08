package com.radar.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.radar.extend.IosTokenDao;

/**
 * IOS设备的deviceToken值的管理：包括deviceToken的保存(更新)、获取、删除
 * @ClassName:  DeviceToken   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午6:17:09
 */
public class DeviceToken {
	 private static final Logger log = LoggerFactory.getLogger(DeviceToken.class);
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
		} else if(StringUtils.isEmpty(token) && StringUtils.isEmpty(userName)){//新加用户
			try {
				IosTokenDao.getInstance().saveIosToken(key, value);
			} catch (SQLException e) {
				log.error("保存iostoken失败1:"+e.getMessage());
			}
			return;
		}else {
			//此处保证IOS一个设备对应一个username
			//如果一个username可以多出登陆测另外计算
			try{
			IosTokenDao.getInstance().delTokenByUser(key);
			IosTokenDao.getInstance().delUserByToken(value);
			if(StringUtils.isNotEmpty(token)){
				IosTokenDao.getInstance().delUserByToken(token);
			}
			if(StringUtils.isNotEmpty(userName)){
				IosTokenDao.getInstance().delTokenByUser(userName);
			}
			}catch(SQLException e){
				log.error("删除iostoken失败:"+e.getMessage());
			}
		}
		
		//其他情况下保存deviceToken
		try{
		IosTokenDao.getInstance().saveIosToken(key, value);
		}catch(SQLException e){
			log.error("保存iostoken失败2:"+e.getMessage());
		}
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
		try{
			
		 return IosTokenDao.getInstance().getTokenByUserName(key);
		 
		}catch(SQLException e){
			log.error("更具key获取token失败:"+e.getMessage());
		}
		return null;
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
		List<String> list =new ArrayList<String>();
		try{
			 list.addAll(IosTokenDao.getInstance().getAllUser().keySet());
			}catch(SQLException e){
				log.error("更具key获取token失败:"+e.getMessage());
			}
		return list;
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
		List<String> list =new ArrayList<String>();
		try{
			 list.addAll(IosTokenDao.getInstance().getAllUser().values());
			}catch(SQLException e){
				log.error("根据key获取token失败:"+e.getMessage());
			}
		return list;
	}
	
	public static String getUserNameByToken(String token){
		if(StringUtils.isEmpty(token)){
			return null;
		}
		
		token = token.trim();
		try{
		 return IosTokenDao.getInstance().getUserNameByToken(token);
		}catch(SQLException e){
			log.error("根据token获取userName失败:"+e.getMessage());
		}
		return null;
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
		try{
			
		if(!StringUtils.isEmpty(token)){
			IosTokenDao.getInstance().delUserByToken(token);
		}
		IosTokenDao.getInstance().delTokenByUser(key);
		}catch(SQLException e){
			log.error("删除iostoken失败:"+e.getMessage());
		}
	}
}