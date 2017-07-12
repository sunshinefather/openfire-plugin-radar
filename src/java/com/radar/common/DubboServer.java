package com.radar.common;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

public class DubboServer {
private static DubboServer dubbo =new DubboServer();
private ApplicationConfig application = new ApplicationConfig();
private RegistryConfig registry = new RegistryConfig();
private DubboServer(){
	application.setName("yunbao-openfire");
    registry.setProtocol("zookeeper");
    registry.setAddress(EnvConstant.ZOOKEEPERHOST);
}
public static DubboServer getInstance(){
	return dubbo;
}

public <T> T getService(Class<T> t){
	ReferenceConfig<T> rc = new ReferenceConfig<T>();
    rc.setApplication(application);
    rc.setRegistry(registry);
    rc.setProtocol("dubbo");
    rc.setTimeout(10000);
    rc.setInterface(t);
    //防止重复创建链接,出现内存泄漏
    ReferenceConfigCache cache =ReferenceConfigCache.getCache();
    T service =cache.get(rc);
    return service;
}

//销毁
public void destroyAll(){
	ProtocolConfig.destroyAll();
}
}