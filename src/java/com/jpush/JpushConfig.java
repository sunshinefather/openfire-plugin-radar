package com.jpush;

import com.radar.ios.PushMessage;

public class JpushConfig {
  //孕宝
  public static final String MOM_APPKEY=PushMessage.IOS_PRODUCT_ENV?"2b2d440d7025e9b22efe36f6":"1a91516c85b5d10ba59a6ec7";//内网:1a91516c85b5d10ba59a6ec7;外网:2b2d440d7025e9b22efe36f6
  public static final String MOM_MASTER_SECRET=PushMessage.IOS_PRODUCT_ENV?"57a8203808cdc5d1c6dc9e6a":"c2bee5a8b8ede3a5e07c20b2";//内网:c2bee5a8b8ede3a5e07c20b2;外网:57a8203808cdc5d1c6dc9e6a
 
  //孕宝医生
  public static final String DOCTOR_APPKEY=PushMessage.IOS_PRODUCT_ENV?"7a54e0a24b623f7ecdff2447":"b0d0bcfad5c428ad6279b64c";//内网:b0d0bcfad5c428ad6279b64c;外网:7a54e0a24b623f7ecdff2447
  public static final String DOCTOR_MASTER_SECRET=PushMessage.IOS_PRODUCT_ENV?"7cda93da3c1ea1065765d585":"2d65d761bb1237e2da4c9bd2";//内网:2d65d761bb1237e2da4c9bd2;外网:7cda93da3c1ea1065765d585
 
}
