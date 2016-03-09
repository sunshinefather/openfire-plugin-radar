package com.radar.common;

import java.lang.reflect.Constructor;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

/**
 * Client管理器
 * @ClassName:  ThriftClientManager   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年3月6日 下午1:21:48
 */
public class ThriftClientManager {
	/**
	 * 获取单协议单接口Client,调用cas,用户中心系统使用
	 * @Title: getClient
	 * @Description: TODO  
	 * @param: @param nimbusHost
	 * @param: @param nimbusPort
	 * @param: @return
	 * @param: @throws TTransportException      
	 * @return: ThriftClientInfo
	 * @author: sunshine  
	 * @throws
	 */
	public static ThriftClientInfo getClient(String host, int port,Class<? extends TServiceClient> classes) throws Exception {
		ThriftClientInfo  client=new ThriftClientInfo();
		TSocket tsocket = new TSocket(host, port);
		TFramedTransport ttransport=new TFramedTransport(tsocket);
		TBinaryProtocol tprotocol=new TBinaryProtocol(ttransport);
		client.setTsocket(tsocket);
		client.setTtransport(ttransport);
		client.setTprotocol(tprotocol);
		Constructor<?> constructor = classes.getConstructor(TProtocol.class);
		TServiceClient tc=(TServiceClient)constructor.newInstance(tprotocol);
		client.setTserviceClient(tc);
		ttransport.open();
		return client; 

	}
	/**
	 * 获取多协议多接口Client,调用crm系统使用
	 * @Title: getClient
	 * @Description: TODO  
	 * @param: @param host
	 * @param: @param port
	 * @param: @param classes
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: ThriftClientInfo
	 * @author: sunshine  
	 * @throws
	 */
	public static ThriftClientInfo getExpendClient(String host, int port,Class<? extends TServiceClient> classes) throws Exception {
		ThriftClientInfo  client=new ThriftClientInfo();
		TSocket tsocket = new TSocket(host, port);
		TFramedTransport ttransport=new TFramedTransport(tsocket);
		TMultiplexedProtocol tprotocol =new TMultiplexedProtocol(new TBinaryProtocol(ttransport),getDefName(classes));
		client.setTsocket(tsocket);
		client.setTtransport(ttransport);
		client.setTprotocol(tprotocol);
		Constructor<?> constructor = classes.getConstructor(TProtocol.class);
		TServiceClient tc=(TServiceClient)constructor.newInstance(tprotocol);
		client.setTserviceClient(tc);
		ttransport.open();
		return client; 

	}
	/**
	 * 关闭连接
	 * @Title: closeClient
	 * @Description: TODO  
	 * @param: @param client      
	 * @return: void
	 * @author: sunshine  
	 * @throws
	 */
	public static void closeClient(ThriftClientInfo client) {
        if (null == client) {  
            return;  
        }  
          
        if (null != client.getTtransport()) {  
            client.getTtransport().close();  
        }  
          
        if (null != client.getTsocket()) {  
            client.getTsocket().close();  
        }  
    }
    /**
     * 自定义多协议接口名称
     * @Title: getDefName
     * @Description: TODO  
     * @param: @param obj
     * @param: @return      
     * @return: String
     * @author: sunshine  
     * @throws
     */
    private static String getDefName(Class<? extends TServiceClient> classes){
       return classes.getName().split("[$]")[0];
    }
}
