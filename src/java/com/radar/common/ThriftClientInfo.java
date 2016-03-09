package com.radar.common;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
/**
 * thrift 客户端封装
 * @ClassName:  ThriftClientInfo   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年3月6日 下午1:16:53
 */
public class ThriftClientInfo {
	private TSocket tsocket;
	private TTransport ttransport;
	private TProtocol tprotocol;
	private TServiceClient tserviceClient;
	public TSocket getTsocket() {
		return tsocket;
	}
	public void setTsocket(TSocket tsocket) {
		this.tsocket = tsocket;
	}
	public TTransport getTtransport() {
		return ttransport;
	}
	public void setTtransport(TTransport ttransport) {
		this.ttransport = ttransport;
	}
	public TProtocol getTprotocol() {
		return tprotocol;
	}
	public void setTprotocol(TProtocol tprotocol) {
		this.tprotocol = tprotocol;
	}
	public TServiceClient getTserviceClient() {
		return tserviceClient;
	}
	public void setTserviceClient(TServiceClient tserviceClient) {
		this.tserviceClient = tserviceClient;
	}
	
}
