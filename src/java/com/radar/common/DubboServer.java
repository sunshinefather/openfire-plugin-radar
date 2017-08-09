package com.radar.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

public class DubboServer {
private static DubboServer dubbo =new DubboServer();
private ApplicationConfig application = new ApplicationConfig();
private RegistryConfig registry = new RegistryConfig();
private static Map<Class<?>, ReferenceConfig<?>> referenceConfigMap = new ConcurrentHashMap<Class<?>, ReferenceConfig<?>>();
private DubboServer(){
	application.setName("yunbao-openfire");
    registry.setProtocol("zookeeper");
    registry.setAddress(EnvConstant.ZOOKEEPERHOST);
    registry.setCheck(false);
}
public static DubboServer getInstance(){
	return dubbo;
}

@SuppressWarnings("unchecked")
public <T> T getService(Class<T> t){
	//下面这句话完美解决dubbo问题
	//Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
	ReferenceConfig<?> rc=referenceConfigMap.get(t);
	if(rc==null){
		rc = getReferenceConfig(t);
	    referenceConfigMap.put(t,rc);
	}
    //防止重复创建链接,出现内存泄漏
    ReferenceConfigCache cache =ReferenceConfigCache.getCache();
    Object service =cache.get(rc);
    return (T)service;
}

private <T> ReferenceConfig<T> getReferenceConfig(Class<T> clazz) {
    ReferenceConfig<T> reference = new ReferenceConfig<T>();
    reference.setApplication(application);
    reference.setRegistry(registry);
    reference.setProtocol("dubbo");
    reference.setRetries(3);
    reference.setInterface(clazz);
    reference.setTimeout(10000);
    return reference;
}

//销毁
public void destroyAll(){
	ProtocolConfig.destroyAll();
}
}