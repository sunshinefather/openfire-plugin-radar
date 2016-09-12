package com.radar.broadcast;

import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;
import org.xmpp.packet.Presence;
import com.jpush.android.DoctorClientPush;
import com.jpush.android.MomClientPush;
import com.radar.common.EnvConstant;
import com.radar.extend.OfflineDao;
import com.radar.utils.HixinUtils;
/**
 * 广播消息发射器
 * @ClassName:  BoardcastEmitter   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年2月2日 下午2:42:28
 */
public class BoardcastEmitter
{    
	private static OfflineDao messageStore = OfflineDao.getInstance();
    private static final Logger log = LoggerFactory.getLogger(BoardcastEmitter.class);
    /**
     * 发送Message定向广播消息, 接收人不在线走离线
     * @Title: sendBoardCastAndStoreServer
     * @Description: TODO  
     * @param: @param userName 接收人
     * @param: @param message  发送内容
     * @return: void
     * @author: sunshine  
     * @throws
     */
    public static boolean sendBoardCastAndStoreServer(String userName,Message message){
        if(StringUtils.isNotEmpty(userName)){
        	Collection<Presence> listOnline = XMPPServer.getInstance().getPresenceManager().getPresences(userName);
        	if(listOnline==null || listOnline.isEmpty()){
                message.setTo(userName +"@"+HixinUtils.getDomain());
                messageStore.addMessage(message);
        	}else{
        		 XMPPServer.getInstance().getSessionManager().userBroadcast(userName, message);
        	}
        	 try {
					User user= XMPPServer.getInstance().getUserManager().getUser(userName);
					PacketExtension pext= message.getExtension("notice", "notice:extension:type");
					if(pext!=null ){
						String dateType = pext.getElement().elementTextTrim("type");
						if(StringUtils.isNotEmpty(dateType)){
							if(user!=null && "300".equals(user.getEmail())){
				        		MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType,new String[]{userName});
				        	}else if(user!=null &&  "201".equals(user.getEmail())){
				        		DoctorClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType,new String[]{userName});
				        	}
						}
					}
	
				} catch (UserNotFoundException e) {
					e.printStackTrace();
				}
            return true;
        }else{
            log.debug(userName+"：发送广播对象不存在");
            return false;
        }
    }
    /**
     * 发送Message广播消息
     * @Title: sendBoardCastServer
     * @Description: TODO  
     * @param: @param message
     * @param: @return      
     * @return: boolean
     * @author: sunshine  
     * @throws
     */
    public static boolean sendBoardCastServer(String appName,String accepterType,Message message){
    	//XMPPServer.getInstance().getSessionManager().broadcast(message);
    	boolean tag =false;
    	Collection<ClientSession> ccsess= XMPPServer.getInstance().getSessionManager().getSessions();
    	UserManager userManager =  XMPPServer.getInstance().getUserManager();
    	PacketExtension pext= message.getExtension("notice", "notice:extension:type");
    	String dateType =null;
		if(pext!=null ){
			dateType = pext.getElement().elementTextTrim("type");
		}
    	if(EnvConstant.APPS.ALLAPP.toString().equals(appName)){
    		XMPPServer.getInstance().getSessionManager().broadcast(message);
    		if(StringUtils.isNotEmpty(dateType)){
        		MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
        		DoctorClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
    		}
	    }else{
	    	//极光推送
	    	if(StringUtils.isEmpty(accepterType) && StringUtils.isEmpty(appName)){
	    		if(StringUtils.isNotEmpty(dateType)){
	    			MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
	    		}
		       }else if(StringUtils.isNotEmpty(accepterType)){
		    	   if(StringUtils.isNotEmpty(dateType)){
		    		    if("300".equals(accepterType)){
		    		    	MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
		    		    }else if("201".equals(accepterType)){
		    		    	DoctorClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
		    		    }
		    		}
		       }else if(StringUtils.isNotEmpty(appName)){
		    	   EnvConstant.APPS app =EnvConstant.APPS.valueOf(appName);
		    	   if(app == EnvConstant.APPS.MOM){
		    		   MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
		    	   }else if(app == EnvConstant.APPS.DOCTOR){
		    		   DoctorClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType);
		    	   }
			    }
	    	//极光推送结束
	    	//xmpp推送
	    	for(ClientSession sess:ccsess){
        		try {
    				String userName = sess.getUsername();
    				if(StringUtils.isNotEmpty(userName)){
    					User user = userManager.getUser(userName);
    					 if(user!=null){
    						 if(StringUtils.isEmpty(accepterType) && StringUtils.isEmpty(appName)){
    	    					 if(EnvConstant.defaultAccepterType.toString().equals(user.getEmail())){//email目前用作业务用户类型使用
    	    						 XMPPServer.getInstance().getSessionManager().userBroadcast(userName, message);
    	    					 }
    					       }else if(StringUtils.isNotEmpty(accepterType)){
    					    	   if(accepterType.equals(user.getEmail())){//email目前用作业务用户类型使用
      	    						 XMPPServer.getInstance().getSessionManager().userBroadcast(userName, message);
      	    					   }
    					       }else if(StringUtils.isNotEmpty(appName)){
    					    	   EnvConstant.APPS app =EnvConstant.APPS.valueOf(appName);
    					    	   if(app == EnvConstant.APPS.MOM){
    					    		   if("300".equals(user.getEmail())){//email目前用作业务用户类型使用
    	      	    						 XMPPServer.getInstance().getSessionManager().userBroadcast(userName, message);
    	      	    					   }
    					    	   }else if(app == EnvConstant.APPS.DOCTOR){
    					    		   if("201".equals(user.getEmail())){//email目前用作业务用户类型使用
  	      	    						 XMPPServer.getInstance().getSessionManager().userBroadcast(userName, message);
  	      	    					   }
    					    	   }
    						    }
    					 }
    				}
    			} catch (UserNotFoundException e) {
    				e.printStackTrace();
    			}
        	}
	    }
    	return tag;
    }
    
    /**
     * 向所有在线用户发送系统广播消息
     * @Title: sendServerMessage
     * @Description: TODO  
     * @param: @param subject
     * @param: @param body
     * @param: @return      
     * @return: boolean
     * @author: sunshine  
     * @throws
     */
    public static boolean sendServerMessage(String subject,String body){
    	XMPPServer.getInstance().getSessionManager().sendServerMessage(subject, body);
    	return true;
    }
    
    /**
     * 发送定向消息, 接收人不在线不处理
     * @Title: sendBoardCastServer
     * @Description: TODO  
     * @param: @param userName 接收人
     * @param: @param iq      发送内容
     * @return: void
     * @author: sunshine  
     * @throws
     */
    public static boolean sendBoardCastServer(String userName,Packet packet){
        if(StringUtils.isNotEmpty(userName)){
        	
        	if(packet instanceof IQ){
        		((IQ) packet).setType(IQ.Type.result);
        	}else if(packet instanceof Message){
        		Message message =(Message)packet;
        		try {
					User user= XMPPServer.getInstance().getUserManager().getUser(userName);
					PacketExtension pext= message.getExtension("notice", "notice:extension:type");
					if(pext!=null ){
						String dateType = pext.getElement().elementTextTrim("type");
						if(StringUtils.isNotEmpty(dateType)){
							if(user!=null && "300".equals(user.getEmail())){
				        		MomClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType,new String[]{userName});
				        	}else if(user!=null &&  "201".equals(user.getEmail())){
				        		DoctorClientPush.push(message.getSubject(),message.getBody(),message.getFrom().getNode(), dateType, dateType,new String[]{userName});
				        	}
						}
					}
	
				} catch (UserNotFoundException e) {
					e.printStackTrace();
				}
        	}
        	XMPPServer.getInstance().getSessionManager().userBroadcast(userName, packet);
            return true;
        }else{
            log.debug(userName+"：发送定向消息接收者为空");
            return false;
        }
    }
}