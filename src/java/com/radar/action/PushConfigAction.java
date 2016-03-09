package com.radar.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.radar.common.EnvConstant;
import com.radar.common.ThriftClientInfo;
import com.radar.common.ThriftClientManager;
import com.zyt.web.after.push.remote.ImCrmPushConfig;
import com.zyt.web.after.push.remote.ImCrmPushService;

public class PushConfigAction {
	private static final String HOST=EnvConstant.IMCRMHOST;
	private static final int PORT=EnvConstant.IMCRMPORT;
	/**
	 * 切换推送(推送与不推送 每调用一次,状态进行切换)
	 * @Title: switchPush
	 * @Description: TODO  
	 * @param: @param pushConfig
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean  switchPush(ImCrmPushConfig pushConfig){
		boolean falg=false;
		ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmPushService.Client.class);
			ImCrmPushService.Client clent=(ImCrmPushService.Client)clientinfo.getTserviceClient();
			pushConfig=clent.savePushConfig(pushConfig);
			if(StringUtils.isNotEmpty(pushConfig.getId())){
				falg=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
		return falg;
	}
	
	/**
	 * 查询推送配置
	 * @Title: findPushConfig
	 * @Description: TODO  
	 * @param: @param pushConfig
	 * @param: @return      
	 * @return: List<ImCrmPushConfig>
	 * @author: sunshine  
	 * @throws
	 */
	public static List<ImCrmPushConfig>  findPushConfig(ImCrmPushConfig pushConfig){
       List<ImCrmPushConfig>  list =new ArrayList<ImCrmPushConfig>();
       ThriftClientInfo clientinfo=null;
		try {
			clientinfo = ThriftClientManager.getExpendClient(HOST, PORT, ImCrmPushService.Client.class);
			ImCrmPushService.Client clent=(ImCrmPushService.Client)clientinfo.getTserviceClient();
			list=clent.findPushConfig(pushConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ThriftClientManager.closeClient(clientinfo);
		}
       return list;
	}
}