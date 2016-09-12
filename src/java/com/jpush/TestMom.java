package com.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class TestMom {
    private static final String APPKEY="2b2d440d7025e9b22efe36f6";
    private static final String MASTER_SECRET ="57a8203808cdc5d1c6dc9e6a";
    
    public static PushPayload buildPushObject_ios_alias_alert(String userName) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(userName))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
                                .setAlert("中国人不是好欺负的哈哈12345")
                                .setBadge(5)
                                .setSound("default.caf")
                                .addExtra("dataId", "46e3a209-8832-46c6-9842-0fd296f01140")
                                .addExtra("senderId", "1f4ebd0bdd964ee790288d93f33759a9/18980678281")
                                .addExtra("dataType",2)
                                .addExtra("messageType",0)
                                .build())
                        .build())
                        //.setMessage(Message.content("测试124hello"))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)
                         .build())
                 .build();
    }
	public static void main(String[] args) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APPKEY,3);
        PushPayload payload = buildPushObject_ios_alias_alert("111111");
       // System.out.println(payload.toString());
        try {
           PushResult result = jpushClient.sendPush(payload);
           System.out.println(result.getResponseCode());
        } catch (APIConnectionException e) {
        	System.out.println("不能连接到极光推送服务器");
        } catch (APIRequestException e) {
        	System.out.println(String.format("http status: %s ,error code: %s,error message: %s",e.getStatus(),e.getErrorCode(),e.getErrorMessage()));
        }
	}

}
