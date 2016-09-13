package com.jpush.ios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radar.ios.PushMessage;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class DoctorClientPush {
    private static final String APPKEY="7a54e0a24b623f7ecdff2447";
    private static final String MASTER_SECRET ="7cda93da3c1ea1065765d585";
    private static final ClientConfig clientConfig=ClientConfig.getInstance();
    private static final JPushClient jpushClient;
    private static final Logger Log = LoggerFactory.getLogger(DoctorClientPush.class);
    static{
    	clientConfig.setConnectionTimeout(10*1000);
    	jpushClient = new JPushClient(MASTER_SECRET, APPKEY,null,clientConfig);
    }
    public static PushPayload buildPushObject_ios_alias(String[] userName,String alert,String dataId,String senderId,String dataType,String messageType) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(userName))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                //.setBadge(1)
                                .setSound("default.caf")
                                .addExtra("dataId",dataId)
                                .addExtra("senderId",senderId)
                                .addExtra("dataType",dataType)
                                .addExtra("messageType",messageType)
                                .build())
                        .build())
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(PushMessage.IOS_PRODUCT_ENV)
                         .build())
                 .build();
    }
    public static PushPayload buildPushObject_ios_all(String alert,String dataId,String senderId,String dataType,String messageType) {
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.ios())
    			.setAudience(Audience.all())
    			.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
    					.setAlert(alert)
    					//.setBadge(1)
    					.setSound("default.caf")
    					.addExtra("dataId",dataId)
    					.addExtra("senderId",senderId)
    					.addExtra("dataType",dataType)
    					.addExtra("messageType",messageType)
    					.build())
    					.build())
    					.setOptions(Options.newBuilder()
    							.setApnsProduction(PushMessage.IOS_PRODUCT_ENV)
    							.build())
    							.build();
    }
    public static void push(final String alert,final String dataId,final String senderId,final String dataType,final String messageType, final String[]... userName){
    	ThreadPool.addWork(new QueueTask() {
			@Override
			public void executeTask() throws Exception {
				//JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY,null,clientConfig);
		    	try{
		    	if(userName!=null && userName.length>0){
		        	jpushClient.sendPush(buildPushObject_ios_alias(userName[0], alert, dataId, senderId, dataType, messageType));
		    	}else{
		        	jpushClient.sendPush(buildPushObject_ios_all(alert, dataId, senderId, dataType, messageType));
		    	}
		        } catch (APIConnectionException e) {
		        	Log.error("ios doctor不能连接到极光推送服务器",e);
		        } catch (APIRequestException e) {
		        	Log.error(String.format("ios doctor http status: %s ,error code: %s,error message: %s",e.getStatus(),e.getErrorCode(),e.getErrorMessage()));
		        }
			}
		});
    }
}
