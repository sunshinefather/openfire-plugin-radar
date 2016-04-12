package com.radar.common;

import java.util.Collection;
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
	public static  String IMCRMHOST="192.168.43.154";//(内网内)192.168.253.235;(华西内)192.168.43.154
     /**
      * imcrm服务器端口
      */
	public static int IMCRMPORT=7700;
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
}
