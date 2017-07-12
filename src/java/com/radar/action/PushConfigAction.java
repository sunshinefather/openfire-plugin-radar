package com.radar.action;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.mdks.imcrm.bean.PushConfig;
import com.mdks.imcrm.service.PushRpcService;
import com.radar.common.DubboServer;

public class PushConfigAction {
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
	public static boolean  switchPush(PushConfig pushConfig){
		boolean falg=false;
		try {
			PushRpcService clent = DubboServer.getInstance().getService(PushRpcService.class);
			pushConfig=clent.savePushConfig(pushConfig);
			if(StringUtils.isNotEmpty(pushConfig.getId())){
				falg=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return falg;
	}
	
	/**
	 * 查询推送配置
	 * @Title: findPushConfig
	 * @Description: TODO  
	 * @param: @param pushConfig
	 * @param: @return      
	 * @return: List<PushConfig>
	 * @author: sunshine  
	 * @throws
	 */
	public static List<PushConfig>  findPushConfig(PushConfig pushConfig){
       List<PushConfig>  list =new ArrayList<PushConfig>();
		try {
			PushRpcService clent = DubboServer.getInstance().getService(PushRpcService.class);
			list=clent.findPushConfig(pushConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return list;
	}
}