package com.radar.broadcast.contact;

import java.util.Set;

import com.radar.pool.ThreadPool;
/**
 * 发送更新通讯录和群组通讯录的广播工具类
 * @ClassName:  ContactBroadcast   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:38:07
 */
public class ContactBroadcast {
    /**
     * 发送群组更新通讯录广播
     * @Title: sendGroupBorad
     * @Description: TODO  
     * @param: @param groupUid      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public static void sendGroupBorad(String groupUid) {
		ThreadPool.addWork(new SendGroupBoradTask(groupUid));
	}
    /**
     * 单个发送通讯录更新广播
     * @Title: sendGroupBoradJID
     * @Description: TODO  
     * @param: @param items      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public static void sendGroupBoradJID(Set<String> items) {
		ThreadPool.addWork(new SendGroupBoradTask(items));
	}
}
