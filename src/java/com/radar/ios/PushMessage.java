package com.radar.ios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;
import com.apns.IApnsService;
import com.apns.impl.ApnsServiceImpl;
import com.apns.model.ApnsConfig;
import com.apns.model.Feedback;
import com.apns.model.Payload;
import com.jpush.ios.DoctorClientPush;
import com.jpush.ios.MomClientPush;
import com.mdks.imcrm.bean.GroupRoom;
import com.mdks.imcrm.bean.PushConfig;
import com.radar.action.ContactAction;
import com.radar.action.DeviceToken;
import com.radar.action.GroupAction;
import com.radar.action.PushConfigAction;
import com.radar.common.EnvConstant;
import com.radar.extend.IosTokenDao;
import com.radar.extend.PaginationAble;

/**
 * IOS设备推送消息
 * 分为1对1推送,群聊推送和通知推送
 * @ClassName:  PushMessage   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年6月30日 上午10:14:27
 */
public class PushMessage {
	
	private static final Logger log = LoggerFactory.getLogger(PushMessage.class);
	/**证书推送环境*/
	public static final boolean  IOS_PRODUCT_ENV=true;

	/** start 环境配置  */
	private static String KSPASSWORD="1234";// 证书密码
	
	private static String MOM_CERTIFICATE_URL=PushMessage.class.getResource("mom_push_product_99.p12").getPath();
	private static String DOCTOR_CERTIFICATE_URL=PushMessage.class.getResource("doctor_push_product_99.p12").getPath();
	/** end 环境配置  */
	
	public static IApnsService serviceMom;
	public static IApnsService serviceDoctor;
	
	static{
		if(!PushMessage.IOS_PRODUCT_ENV){
			MOM_CERTIFICATE_URL=PushMessage.class.getResource("mom_push_dev_99.p12").getPath();
			DOCTOR_CERTIFICATE_URL=PushMessage.class.getResource("doctor_push_dev_99.p12").getPath();
		}
		ApnsConfig mom_config99 = new ApnsConfig();
		ApnsConfig doctor_config99 = new ApnsConfig();
		try {
			InputStream mom_is = new FileInputStream(MOM_CERTIFICATE_URL);
			mom_config99.setKeyStore(mom_is);
			mom_config99.setDevEnv(!PushMessage.IOS_PRODUCT_ENV);
			mom_config99.setPassword(KSPASSWORD);
			mom_config99.setPoolSize(12);
			mom_config99.setName("mom_app");
			serviceMom= ApnsServiceImpl.createInstance(mom_config99);
			
			InputStream doctor_is = new FileInputStream(DOCTOR_CERTIFICATE_URL);
			doctor_config99.setKeyStore(doctor_is);
			doctor_config99.setDevEnv(!PushMessage.IOS_PRODUCT_ENV);
			doctor_config99.setPassword(KSPASSWORD);
			doctor_config99.setPoolSize(15);
			doctor_config99.setName("doctor_app");
			serviceDoctor= ApnsServiceImpl.createInstance(doctor_config99);
		} catch (FileNotFoundException e) {
			log.error("@sunshine:启动apns推送池失败 "+e.getMessage(),e);
		}
	}
    /**
     * 1对1推送消息
     * @Title: pushChatMessage
     * @Description: TODO  
     * @param: @param message
     * @param: @throws Exception      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public static void pushChatMessage(Message message) throws Exception{
		String sendUser = message.getFrom().getNode();
    	String deviceToken = DeviceToken.get(message.getTo().getNode(),true);
    	
		String sendUserName = ContactAction.queryNickeName(sendUser);
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), sendUserName + ":", message.getBody()));
		param.addParam("senderId",sendUser);
		param.addParam("dataType",1);
		param.addParam("dataId",message.getID());
		User user= XMPPServer.getInstance().getUserManager().getUser(message.getTo().getNode());
    	// 没有获取到token值，不发生推送消息
        if(StringUtils.isEmpty(deviceToken)){
        	if("300".equals(user.getEmail())){
				MomClientPush.push(param.getAlert(),message.getID(), sendUser,"1","1",new String[]{message.getTo().getNode()});
			}else if("201".equals(user.getEmail())){
				DoctorClientPush.push(param.getAlert(),message.getID(), sendUser,"1","1",new String[]{message.getTo().getNode()});
			}
        }else{
			push(user,param, deviceToken);
			clearInvalidToken();
		}
	}
	
	public static void pushNoticeMessage(final String appName,final String accepterType,final Message message) throws Exception{
		String msgType;
		 String sendUserName="";
		if(message.getType()==Message.Type.headline){
			PacketExtension pkg=message.getExtension("notice", "notice:extension:type");
			if(pkg!=null && StringUtils.isNotEmpty(pkg.getElement().elementTextTrim("type"))){
				msgType=pkg.getElement().elementTextTrim("type");
			}else{
				return;
			}
			JID jid=message.getTo();
			if(null==jid || StringUtils.isEmpty(jid.getNode())){//全体推送
				if(StringUtils.isEmpty(accepterType) && StringUtils.isEmpty(appName)){
					MomClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
				}else if(StringUtils.isNotEmpty(accepterType)){
					if("300".equals(accepterType)){
						MomClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
					}else if("201".equals(accepterType)){
						DoctorClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
					}
					
				}else if(StringUtils.isNotEmpty(appName)){
					EnvConstant.APPS app =EnvConstant.APPS.valueOf(appName);
					 if(app == EnvConstant.APPS.MOM){
						 MomClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
					 }else if(app == EnvConstant.APPS.DOCTOR){
						 DoctorClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
					 }else if(app == EnvConstant.APPS.ALLAPP){
						 MomClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
						 DoctorClientPush.push(alertMessage(message.getSubject(), sendUserName, message.getSubject()), message.getBody(), message.getFrom().getNode(), msgType, msgType);
					 }
				}
				
				sendUserName="";
				//分页推送通知
				boolean flag=true;
				PaginationAble page =new PaginationAble();
				while(flag){
					page= DeviceToken.getDeviceTokensByPage(page);
					List<?> list=page.getResults();
					if(list!=null && !list.isEmpty()){
						if(log.isDebugEnabled()){
						  log.debug("@sunshine:apns应推送通知"+page.getCurrentResult()+"/"+page.getTotalResults()+"条,"+message.getSubject());
						}
						 for(final Object str:list){
							Payload param=new Payload();
							param.setAlert(alertMessage(message.getSubject(), sendUserName, message.getSubject()));
							param.addParam("senderId",message.getFrom().getNode());
							param.addParam("dataType",msgType);
							param.addParam("dataId",message.getBody());
							String[] userAndToken =str.toString().split("[,]");
							if(userAndToken!=null && userAndToken.length>=2){
								String userName=userAndToken[0];
								String token=userAndToken[1];
								try{
								User user= XMPPServer.getInstance().getUserManager().getUser(userName);
								if(user!=null){
									if(StringUtils.isEmpty(accepterType) && StringUtils.isEmpty(appName)){
										if("300".equals(user.getEmail())){
											push(user, param, token);
										}
									}else if(StringUtils.isNotEmpty(accepterType)){
										if(user.getEmail().equals(accepterType)){
											push(user, param, token);
										}
									}else if(StringUtils.isNotEmpty(appName)){
										EnvConstant.APPS app =EnvConstant.APPS.valueOf(appName);
										 if(app == EnvConstant.APPS.MOM){
											 if("300".equals(user.getEmail())){
												 push(user, param, token);			 
											 }
										 }else if(app == EnvConstant.APPS.DOCTOR){
											 if("201".equals(user.getEmail())){
												 push(user, param, token);			 
											 }
										 }else if(app == EnvConstant.APPS.ALLAPP){
											 push(user, param, token);
										 }
									}
								}
								}catch(Exception e){
									log.error("@sunshine 找不到用户:"+userName,e);
								}
							}
						}
						if(log.isDebugEnabled()){
						   log.debug("@sunshine:apns已推送通知"+page.getCurrentResult()+"/"+page.getTotalResults()+"条,"+message.getSubject());
						  }
						}else{
					   flag=false;
				   }
					if(page.getPageNo()<page.getTotalPages()){
						page.setPageNo(page.getPageNo()+1);
						page.setResults(null);
					}else{
						flag=false;
					}
				}
			}else{//单个推送
				String userName = jid.getNode();
				sendUserName="";
				String deviceToken=DeviceToken.get(message.getTo().getNode(),true);
				
				Payload param=new Payload();
				param.setAlert(alertMessage(message.getSubject(), sendUserName, message.getSubject()));
				param.addParam("senderId",message.getFrom().getNode());
				param.addParam("dataType",msgType);
				param.addParam("dataId",message.getBody());
				try{
				User user= XMPPServer.getInstance().getUserManager().getUser(userName);
				if(StringUtils.isEmpty(deviceToken) || deviceToken.length()<32){
					if("300".equals(user.getEmail())){
						MomClientPush.push(param.getAlert(),message.getBody(),message.getFrom().getNode(),msgType,msgType,new String[]{userName});
					}else if("201".equals(user.getEmail())){
						DoctorClientPush.push(param.getAlert(),message.getBody(),message.getFrom().getNode(),msgType,msgType,new String[]{userName});
					}
				}else{
					push(user,param, deviceToken);
				}
				}catch(Exception e){
					log.error("@sunshine:单个通知推送失败:用户名("+userName+"),推送类型:"+msgType,e);
				}
			}
		}
		clearInvalidToken();
	}
	
    /**
     * 群聊推送消息
     * @Title: pushGroupChatMessage
     * @Description: TODO  
     * @param: @param message
     * @param: @throws Exception      
     * @return: void
     * @author: sunshine  
     * @throws
     */
	public static void pushGroupChatMessage(Message message) throws Exception {
		String groupUid = message.getTo().getNode();
		String from = message.getFrom().getNode();
		GroupRoom groupRoom = GroupAction.getGroupRoomById(groupUid);
		String grouptype="2";
		if(groupRoom!=null && StringUtils.isNotEmpty(groupRoom.getGroupType())){
			grouptype=groupRoom.getGroupType();
		}
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), ContactAction.queryNickeName(from)+":", message.getBody()));
		param.addParam("senderId",groupUid+"/"+from);
		param.addParam("dataType",2);//群聊
		param.addParam("dataId",message.getID());
		
		List<String> itmes = queryGroupMember(groupUid);
		if(itmes.contains(from)){
			PushConfig imCrmPushConfig = new PushConfig();
			imCrmPushConfig.setGroupId(groupUid);
			List<PushConfig> list= PushConfigAction.findPushConfig(imCrmPushConfig);
			List<String> listUserName= new ArrayList<>();//不走apns推送的用户列表
			for(PushConfig _imCrmPushConfig :list){
				listUserName.add(_imCrmPushConfig.getUserName().toLowerCase());
			}
			if(log.isDebugEnabled()){
			  log.debug("@sunshine:apns应推送群聊(包含没有token的和已屏蔽接受推送的)"+itmes.size()+"条,"+message.getBody());
			 }
			  int i=0;
			List<String> list300=new ArrayList<String>();
			List<String> list201=new ArrayList<String>();
			for (String name : itmes) {
				String deviceToken=null;
				if(from.equalsIgnoreCase(name.trim())){
					continue;
				}else {
					deviceToken = DeviceToken.get(name);
				}
				User user= XMPPServer.getInstance().getUserManager().getUser(name);
				if(StringUtils.isNotEmpty(deviceToken) && !listUserName.contains(name.toLowerCase())){
					i++;
					push(user,param, deviceToken);
		        }else if(StringUtils.isEmpty(deviceToken) && !listUserName.contains(name.toLowerCase())){
		        	if("300".equals(user.getEmail())){
		        		list300.add(user.getUsername());
		        	}else if("201".equals(user.getEmail())){
		        		list201.add(user.getUsername());
		        	}
		        }
			}
			if(list300!=null && !list300.isEmpty()){
				String[] users =new String[list300.size()];
				list300.toArray(users);
				MomClientPush.push(param.getAlert(),message.getID(),groupUid+"/"+from,"2",grouptype,users);
			}
			if(list201!=null && !list201.isEmpty()){
				String[] users =new String[list201.size()];
				list201.toArray(users);
				DoctorClientPush.push(param.getAlert(),message.getID(),groupUid+"/"+from,"2",grouptype,users);
			}
			if(log.isDebugEnabled()){
			 log.debug("@sunshine:apns已推送群聊"+i+"条,"+message.getBody());
			 }
			clearInvalidToken();
		}
	}

	/**
     * 根据群组id查询群组成员
     * @param groupUid 群组id
     * @return
     * @throws Exception
     */
    private static List<String> queryGroupMember(String groupUid) throws Exception
    {
       List<String> item = GroupAction.getGroupMemberListWithUserName(groupUid);
        return item == null ? new ArrayList<String>(0) : item;
    }
    
	/**
	 * 推送消息到mom_app
	 * @param param 推送参数
	 * @param deviceToken ios设备deviceToken值
	 */
	private static void push2Mom(Payload param, String deviceToken){
		serviceMom.sendNotification(deviceToken, param);
	}
	
	/**
	 * 推送消息到Doctor_app
	 * @param param 推送参数
	 * @param deviceToken ios设备deviceToken值
	 */
	private static void push2Doctor(Payload param, String deviceToken){
		serviceDoctor.sendNotification(deviceToken, param);
	}
	
	/**
	 * apns推送
	 * @Title: push
	 * @Description: TODO  
	 * @param: @param user
	 * @param: @param param
	 * @param: @param deviceToken      
	 * @return: void
	 * @author: sunshine  
	 * @throws
	 */
	private static void push(User user,Payload param, String deviceToken){
		if(user!=null){
			 if("300".equals(user.getEmail())){
				 //push2Mom(param, deviceToken);
			 }else if("201".equals(user.getEmail())){
				 //push2Doctor(param, deviceToken);
			 }
		}
	}
	
	
	/**
	 * 提示消息
	 * @param subject 消息类型(文本,图片,语音等)
	 * @param headMessage 消息头内容
	 * @param message 消息体内容
	 * @return: String
	 */
	private static String alertMessage(String subject, String headMessage, String message){
		if("other".equals(subject)){
			message = "[检验报告]";
		}else if ("image".equals(subject)) {
			message = "[图片]";
		}else if ("sound".equals(subject)) {
			message = "[语音]";
		}else if ("attachment".equals(subject)) {
			message = "[文件]";
		}else if ("svideo".equals(subject)) {
			message = "[小视频]";
		}else if ("video".equals(subject)) {
			message = "[视频请求]";
		}else if ("voice".equals(subject)) {
			message = "[语音请求]";
		}else if ("divider".equals(subject)) {
			message = "[咨询已结束]";
		}else if ("patient".equals(subject)) {
			message = "[病历]";
		}else {
			return subMessage(headMessage+message);
		}
		
		return headMessage + message;
	}

	/**
	 * 截取消息内容，用于推送显示
	 * @param message 截取的内容
	 * @return 截取后的内容
	 */
	private static String subMessage(String message) {
		if (StringUtils.isNotEmpty(message) && message.length() > 20) {
			message = message.substring(0, 17) + "...";
		} else if (StringUtils.isEmpty(message)) {
			message = "新信息...";
		}
		
		return message;
	}
	
	public static void clearInvalidToken(){
		if(serviceMom!=null){
			List<Feedback>  list = serviceMom.getFeedbacks();
			if(list!=null && !list.isEmpty()){
				for(Feedback fb:list){
					IosTokenDao.getInstance().delUserByToken(fb.getToken());
				}
			}
		}
		if(serviceDoctor!=null){
			List<Feedback>  list = serviceDoctor.getFeedbacks();
			if(list!=null && !list.isEmpty()){
				for(Feedback fb:list){
					IosTokenDao.getInstance().delUserByToken(fb.getToken());
				}
			}
		}
	}
	public static void main(String[] args) {
		/**	*/
		String token = "591f899e2cc270866d47bda56ad8a43f588b2b4613282d82e622227aff68171b";
				       //+",c258538e942bc449bb78d124a5bf98e29d6da93efec48651bcb41d27effccd7a"//黄
				     // + ",fdc8760fd9dc2e17ef338a6c65f8234a8319a8f0781a7f86cbc65111d439c01c"//许
				     // + ",d08e660bf425bfa16be88b564a9b77422e16aaf02022f1cd06226cb2a92e0e2d"//王勇
				     //+ ",33b25c77e4cb4844d42905f5f624385d7c7cecdad5cf3dc0a7bb167ba282f56b,"
				     // +",f8d4794aa2e7b4c49b2b2dd1baa42fc3c2c2172891670839adc09f5ef9673730"//崔
		               //+ ",193858a0e2e2d62271088aef80ad8ff6bd3ebc92928a9ff897e53b0e3de6d491"
		//+ ",0019a78ef1e62005b74ae119fbf30ba3869637fff8cc65f01787edad5aca91e3"//李
		//+ ",3eba95fb65a5e2b9f4a6a60b0353531a24cb1da4c38a5aaae1c9228c6ae293d1";//姜
		Payload payload = new Payload();
		payload.setAlert("推送测试");
		payload.setBadge(1);
		payload.addParam("senderId","cc5d916f1ada4a1b84c1f65a969af804/oeg3pw6hs81z8ht2i1hl7ps5kssi");//12,appts
		payload.addParam("dataType","2");//12
		payload.addParam("dataId","5554920f16074f6e9269e4ad3548cd99");
		String[] tokens = token.split("[,]");
		for(String tk:tokens){
			serviceMom.sendNotification(tk, payload);
		}
		serviceMom.remainConnPoolSize();//目前线程池中有多少条线程,总线程数-剩余线程数=正在使用的线程数据
	  /***/
		/**获取失效token*/
		if(serviceMom!=null){
			List<Feedback>  list = serviceMom.getFeedbacks();
			if(list!=null && !list.isEmpty()){
				for(Feedback fb:list){
					System.out.println("失效token:"+fb.getToken());
				}
			}
		}
		
	}
}