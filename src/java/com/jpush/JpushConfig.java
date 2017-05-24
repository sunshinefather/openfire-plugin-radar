package com.jpush;

import com.radar.ios.PushMessage;

public class JpushConfig {
  //健康四川医生
  public static final String DOCTOR_APPKEY1=PushMessage.IOS_PRODUCT_ENV?"3466f0c73fd797b086b6dd43":"3466f0c73fd797b086b6dd43";//内网:b0d0bcfad5c428ad6279b64c;外网:7a54e0a24b623f7ecdff2447
  public static final String DOCTOR_MASTER_SECRET1=PushMessage.IOS_PRODUCT_ENV?"d1c11dd1068b1c15e660c27b":"d1c11dd1068b1c15e660c27b";//内网:2d65d761bb1237e2da4c9bd2;外网:7cda93da3c1ea1065765d585
 
  //互联网医院(用户端)
  public static final String MOM_APPKEY1=PushMessage.IOS_PRODUCT_ENV?"d58694c1798f776d3d2afa7b":"d58694c1798f776d3d2afa7b";//内网:1a91516c85b5d10ba59a6ec7;外网:2b2d440d7025e9b22efe36f6
  public static final String MOM_MASTER_SECRET1=PushMessage.IOS_PRODUCT_ENV?"ac59e2da335071ff2caae8eb":"ac59e2da335071ff2caae8eb";//内网:c2bee5a8b8ede3a5e07c20b2;外网:57a8203808cdc5d1c6dc9e6a
 
}
