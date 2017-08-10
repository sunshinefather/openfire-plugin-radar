package com.radar.utils;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.XMPPServer;

import com.radar.common.EnvConstant;

public class HixinUtils {
	private static final String HIXIN_RESOURCE = "mobile";
	private static final String SENDING_FLAG = "_sending";
	private static final String SEARCH_URL = EnvConstant.SEARCH_ENGINESURL;
	private static final String HIXIN_DOMAIN = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
	private static final String RECEIPT_NAME ="receipt"; //回执组件域名
	private static final String GROUP_PREFIX ="group."; //回执组件域名
	private static final String GROUP_DOMAIN =GROUP_PREFIX+HIXIN_DOMAIN; //群分发消息 From 使用的域,待处理
	public static final String MESSAGE_RECEIVED = "msg:sendReply";//主题:消息到达服务器回执
	
	/**
	 * 获取搜索引擎url地址
	 * @return
	 */
	public static String getSearchUrl(){
		return SEARCH_URL;
	}
	
	/**
	 * 获取domain
	 * @return
	 */
	public static String getDomain(){
		return HIXIN_DOMAIN;
	}
	
	/**
	 * 群消息域
	 * @return
	 */
	public static String getGroupDomain(){
		return GROUP_DOMAIN;
	} 
	/**
	 * 群聊主机前缀
	 * @Title: getGroupPrefix
	 * @Description:   
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String getGroupPrefix(){
		return GROUP_PREFIX;
	} 
	
	/**
	 * 获取resource
	 * @return
	 */
	public static String getResource(){
		return HIXIN_RESOURCE;
	}
	
	public static String getReceiptName(){
	    return RECEIPT_NAME;
	}
	
	/**
	 * 根据subject判断资源文件是否在发送中
	 * @param subject
	 * @return
	 */
	public static Boolean isSending(String subject){
		if( StringUtils.isNotEmpty(subject) && subject.indexOf(SENDING_FLAG) != -1){
			return true;
		}else {
			return false;
		}
	}
}
