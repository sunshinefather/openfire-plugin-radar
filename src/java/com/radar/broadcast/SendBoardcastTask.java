package com.radar.broadcast;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import com.radar.ios.PushMessageTask;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;
import com.radar.utils.HixinUtils;
/**
 * 发送广播消息队列
 * @ClassName:  MessageBoardcastTask   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午4:52:24
 */
public class SendBoardcastTask implements QueueTask {
    private String userName;
    private Message message;
    private boolean forceNotStore;
    private IQ iq;
    private boolean isIQ = false;
    private String accepterType;
    private String appName;
    private static final Logger log = LoggerFactory.getLogger(SendBoardcastTask.class);
    
    public SendBoardcastTask (String userName,Message message,Boolean... forceNotStore){
    	this.userName = userName;
    	this.message = message;
    	if(forceNotStore!=null && forceNotStore.length>0){
    		this.forceNotStore=forceNotStore[0];
    	}else{
    		this.forceNotStore=false;
    	}
    }
    public SendBoardcastTask (String appName,String accepterType,Message message,Boolean... forceNotStore){
    	this.message = message;
    	this.appName=appName;
    	this.accepterType=accepterType;
    	if(forceNotStore!=null && forceNotStore.length==1){
    		this.forceNotStore=forceNotStore[0];
    	}else{
    		this.forceNotStore=false;
    	}
    }
    
    public SendBoardcastTask (String userName,IQ iq,Boolean... forceNotStore){
    	this.userName = userName;
    	this.iq = iq;
    	if(forceNotStore!=null && forceNotStore.length==1){
    		this.forceNotStore=forceNotStore[0];
    	}else{
    		this.forceNotStore=false;
    	}
    	this.isIQ = true;
    }
    
	@Override
	public void executeTask() throws Exception {
		if(isIQ){
			BoardcastEmitter.sendBoardCastServer(userName, iq);
		}else{
			
			if(StringUtils.isEmpty(userName)){
				log.debug("xmpp通知推送-广播:"+message.toXML());
				BoardcastEmitter.sendBoardCastServer(appName,accepterType,message.createCopy());
			}else{
				message.setTo(userName +"@"+HixinUtils.getDomain());
				log.debug("xmpp通知推送-指定人:"+message.toXML());
				if(forceNotStore){
					BoardcastEmitter.sendBoardCastServer(userName, message.createCopy());
				}else{
					BoardcastEmitter.sendBoardCastAndStoreServer(userName, message.createCopy());
				}
			}
			//IOS推送通知消息
	    	if(message.getType() == Message.Type.headline){
	    		log.error("@sunshine 进入apns推送线程流程："+message.toXML());
	            ThreadPool.addWork(new PushMessageTask(appName,accepterType,message.createCopy()));
			}
		}
	}
}