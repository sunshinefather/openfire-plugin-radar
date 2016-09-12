package com.jpush;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.jpush.ios.DoctorClientPush;

public class TestDoc {
    private static final String APPKEY="7a54e0a24b623f7ecdff2447";
    private static final String MASTER_SECRET ="7cda93da3c1ea1065765d585";
    
    public static PushPayload buildPushObject_ios_alias(String[] userName) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(userName))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
                                .setAlert("哈哈哈哈")
                                .setBadge(5)
                                .setSound("default.caf")
                                .addExtra("dataId", "46e3a209-8832-46c6-9842-0fd296f01140")
                                .addExtra("senderId", "1f4ebd0bdd964ee790288d93f33759a9/18980678281")
                                .addExtra("dataType",2)
                                .addExtra("messageType",0)
                                .build())
                        .build())
                        .setMessage(Message.content("msg测试"))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(false)
                         .build())
                 .build();
    }
    public static PushPayload buildPushObject_android_alias(String[] userName) {
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.alias(userName))
    			.setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
    					.setAlert("哈哈哈哈")
    					.addExtra("dataId", "46e3a209-8832-46c6-9842-0fd296f01140")
    					.addExtra("senderId", "1f4ebd0bdd964ee790288d93f33759a9/18980678281")
    					.addExtra("dataType",4)
    					.addExtra("messageType",0)
    					.build())
    					.build())
    					//.setMessage(Message.content("测试124hello"))
    					//.setOptions(Options.newBuilder()
    							//.setApnsProduction(false)
    							//.build())
    							.build();
    }
    public static PushPayload buildPushObject_ios_all(String[] userName) {
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.ios())
    			.setAudience(Audience.all())
    			.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
    					.setAlert("哈哈哈哈")
    					.setBadge(5)
    					.setSound("default.caf")
    					.addExtra("dataId", "46e3a209-8832-46c6-9842-0fd296f01140")
    					.addExtra("senderId", "1f4ebd0bdd964ee790288d93f33759a9/18980678281")
    					.addExtra("dataType",2)
    					.addExtra("messageType",0)
    					.build())
    					.build())
    					.setMessage(Message.content("msg测试"))
    					.setOptions(Options.newBuilder()
    							.setApnsProduction(false)
    							.build())
    							.build();
    }
    public static PushPayload buildPushObject_android_all(String[] userName) {
    	return PushPayload.newBuilder()
    			.setPlatform(Platform.android())
    			.setAudience(Audience.all())
    			.setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
    					.setAlert("哈哈哈哈")
    					.addExtra("dataId", "46e3a209-8832-46c6-9842-0fd296f01140")
    					.addExtra("senderId", "1f4ebd0bdd964ee790288d93f33759a9/18980678281")
    					.addExtra("dataType",2)
    					.addExtra("messageType",0)
    					.build())
    					.build())
    					.setOptions(Options.newBuilder()
    							.setApnsProduction(false)
    							.build())
    							.build();
    }
	public static void main(String[] args) {
		//com.jpush.all.AllDoctorClientPush.push("测试一下!,请各位医生忽略","1","2","3","4");
		DoctorClientPush.push("hello","1","2","3","4");
		/*
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY);
        PushPayload payload = buildPushObject_android_alias(new String[]{"13000000002"});
        try {
           PushResult result = jpushClient.sendPush(payload);
           System.out.println(result.getResponseCode());
        } catch (APIConnectionException e) {
        	System.out.println("不能连接到极光推送服务器");
        } catch (APIRequestException e) {
        	System.out.println(String.format("http status: %s ,error code: %s,error message: %s",e.getStatus(),e.getErrorCode(),e.getErrorMessage()));
        }*/
	}

}
