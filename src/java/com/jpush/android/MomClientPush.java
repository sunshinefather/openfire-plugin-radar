package com.jpush.android;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpush.JpushConfig;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;

public class MomClientPush {
    private static final String APPKEY=JpushConfig.MOM_APPKEY;
    private static final String MASTER_SECRET =JpushConfig.MOM_MASTER_SECRET;
    private static final String APPKEY1=JpushConfig.MOM_APPKEY1;
    private static final String MASTER_SECRET1 =JpushConfig.MOM_MASTER_SECRET1;
    private static final ClientConfig clientConfig=ClientConfig.getInstance();
    private static final JPushClient jpushClient;
    private static final JPushClient jpushClient1;
    private static final Logger Log = LoggerFactory.getLogger(MomClientPush.class);
    static{
    	clientConfig.setConnectionTimeout(10*1000);
    	jpushClient = new JPushClient(MASTER_SECRET, APPKEY,null,clientConfig);
    	jpushClient1 = new JPushClient(MASTER_SECRET1, APPKEY1,null,clientConfig);
    }
    public static PushPayload buildPushObject_android_alias(String[] userName,String alert,String dataId,String senderId,String dataType,String messageType) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(userName))
                .setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(alert)
                                .addExtra("dataId",dataId)
                                .addExtra("senderId",senderId)
                                .addExtra("dataType",dataType)
                                .addExtra("messageType",messageType)
                                .build())
                        .build())
                 .build();
    }
    public static PushPayload buildPushObject_android_all(String alert,String dataId,String senderId,String dataType,String messageType) {
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.all())
    			.setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
    					.setAlert(alert)
    					.addExtra("dataId",dataId)
    					.addExtra("senderId",senderId)
    					.addExtra("dataType",dataType)
    					.addExtra("messageType",messageType)
    					.build())
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
		        	jpushClient.sendPush(buildPushObject_android_alias(userName[0], alert, dataId, senderId, dataType, messageType));
		    	}else{
		        	jpushClient.sendPush(buildPushObject_android_all(alert, dataId, senderId, dataType, messageType));
		    	}
		        } catch (APIConnectionException e) {
		        	Log.error("孕宝:android 不能连接到极光推送服务器",e);
		        } catch (APIRequestException e) {
		        	Log.error(String.format("孕宝:android http status: %s ,error code: %s,error message: %s",e.getStatus(),e.getErrorCode(),e.getErrorMessage()));
		        }
			}
		});
    	
    	ThreadPool.addWork(new QueueTask() {
    		@Override
    		public void executeTask() throws Exception {
    			//JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY,null,clientConfig);
    			try{
    				if(userName!=null && userName.length>0){
    					jpushClient1.sendPush(buildPushObject_android_alias(userName[0], alert, dataId, senderId, dataType, messageType));
    				}else{
    					jpushClient1.sendPush(buildPushObject_android_all(alert, dataId, senderId, dataType, messageType));
    				}
    			} catch (APIConnectionException e) {
    				Log.error("互联网医院: android 不能连接到极光推送服务器",e);
    			} catch (APIRequestException e) {
    				Log.error(String.format("互联网医院: android http status: %s ,error code: %s,error message: %s",e.getStatus(),e.getErrorCode(),e.getErrorMessage()));
    			}
    		}
    	});
    }
}
