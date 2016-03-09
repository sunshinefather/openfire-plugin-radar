package com.radar.listener;

import java.util.Collections;
import java.util.Map;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventListener;
import org.jivesoftware.util.StringUtils;

import com.radar.common.EnvConstant;
/**
 * 配置文件监听器
 * @ClassName:  PropertyListenerImpl   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年4月14日 上午10:56:08
 */
public class PropertyListenerImpl implements PropertyEventListener
{
    @Override
    public void xmlPropertyDeleted(String property, Map<String, Object> params)
    {
        this.propertyDeleted(property, params);
    }

    @Override
    public void xmlPropertySet(String property, Map<String, Object> params)
    {
       this.propertySet(property, params);
    }
    
    /**
     * 配置属性项
     */
    @Override
	public void propertySet(String property, Map<String, Object> params) {
		if ("plugin.radar.secret".equals(property)) {
			EnvConstant.SECRET = params.get("value").toString();
		} else if ("plugin.radar.allowedIPs".equals(property)) {
			EnvConstant.ALLOWEDIPS = StringUtils.stringToCollection((String) params.get("value"));
		} else if ("plugin.radar.searchEnginesUrl".equals(property)) {
			EnvConstant.SEARCH_ENGINESURL = (String) params.get("value");
		} else if ("plugin.radar.memcachedAddress".equals(property)) {
			EnvConstant.MEMCACHED_ADDRESS = (String) params.get("value");
		}else if ("plugin.iospush.password".equals(property)) {
			EnvConstant.KSPASSWORD = (String) params.get("value");
		}else if ("plugin.iospush.model".equals(property)) {
			EnvConstant.IS_PRODUCT_ENV = (boolean) params.get("value");
		}
	}
    
    /**
     * 属性配置项删除 
     */
    @Override
	public void propertyDeleted(String property, Map<String, Object> params) {
		if ("plugin.radar.secret".equals(property)) {
			EnvConstant.SECRET = "";
		} else if ("plugin.radar.allowedIPs".equals(property)) {
			EnvConstant.ALLOWEDIPS = Collections.emptyList();
		} else if ("plugin.radar.searchEnginesUrl".equals(property)) {
			EnvConstant.SEARCH_ENGINESURL = "";
		} else if ("plugin.radar.memcachedAddress".equals(property)) {
			EnvConstant.MEMCACHED_ADDRESS = "";
		}else if ("plugin.iospush.password".equals(property)) {
			EnvConstant.KSPASSWORD ="1234";
		}else if ("plugin.iospush.model".equals(property)) {
			EnvConstant.IS_PRODUCT_ENV = false;
		}
	}
    
    /**
     * 密钥
     */
    public static void setSecret(String secret) {
        JiveGlobals.setProperty("plugin.radar.secret", secret);
        EnvConstant.SECRET = secret;
    }
    /**
     * 搜索引擎服务地址
     */
    public static void setSearchEnginesUrl(String searchEnginesUrl)
    {
        JiveGlobals.setProperty("plugin.radar.searchEnginesUrl", searchEnginesUrl);
        EnvConstant.SEARCH_ENGINESURL = searchEnginesUrl;
    }
    /**
     * memcached 访问和操作地址
     * @param memcachedAddress
     */
    public static void setMemcachedAddress(String memcachedAddress){
    	JiveGlobals.setProperty("plugin.radar.memcachedAddress", memcachedAddress);
    	EnvConstant.MEMCACHED_ADDRESS = memcachedAddress;
    }
    
    public static void setKSPassword(String ksPassword){
        JiveGlobals.setProperty("plugin.iospush.password", ksPassword);
        EnvConstant.KSPASSWORD = ksPassword;
    }
    
    public static void setProductEnv(boolean productEnv){
    	JiveGlobals.setProperty("plugin.iospush.model", String.valueOf(productEnv));
    	EnvConstant.IS_PRODUCT_ENV = productEnv;
    }
    
}
