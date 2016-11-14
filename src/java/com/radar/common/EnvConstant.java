package com.radar.common;

import java.util.Collection;

import com.radar.ios.PushMessage;
/**
 * 配置参数常量
 * @ClassName:  EnvConstant   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:45:06
 */
public class EnvConstant
{
    /**
     * imcrm服务器地址
     */
	public static final String IMCRMHOST=PushMessage.IOS_PRODUCT_ENV?"192.168.66.20":"192.168.1.245";//(内网内)192.168.1.245;192.168.66.20(华为)
     /**
      * imcrm服务器端口
      */
	public static final int IMCRMPORT=7700;
    /**
     * 搜索引擎访问地址
     */
    public static String SEARCH_ENGINESURL = null;
    /**
     * 钥匙
     */
    public static String SECRET;
    
    /**
     * 允许的IP集合
     */
    public static Collection<String> ALLOWEDIPS;
    /**
     * memcached 访问和操作地址
     */
    public  static String MEMCACHED_ADDRESS ;
    
    /**
     * 证书密码
     */
    public static String KSPASSWORD;
    
    /**
     * 环境
     */
    public static boolean IS_PRODUCT_ENV;
    /**
     * 默认用户类型
     */
    public static String defaultAccepterType="300";
    /**
     * 默认AppName
     */
    public static APPS defaultAppName=APPS.MOM;
    
    public enum APPS{
    	MOM,DOCTOR,ALLAPP
    }
}
