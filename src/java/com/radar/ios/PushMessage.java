package com.radar.ios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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
import com.radar.action.ContactAction;
import com.radar.action.DeviceToken;
import com.radar.action.GroupAction;
import com.radar.action.PushConfigAction;
import com.radar.extend.IosTokenDao;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;
import com.zyt.web.after.push.remote.ImCrmPushConfig;

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

	/** start 环境配置  */
	private static String KSPASSWORD="1234";// 证书密码
	
	private static String CERTIFICATE_URL=PushMessage.class.getResource("mom_push_product_99.p12").getPath();//99证书名称
	/** end 环境配置  */
	
	public static IApnsService service99;
	
	static{
		ApnsConfig config99 = new ApnsConfig();
		try {
			InputStream is = new FileInputStream(CERTIFICATE_URL);
			config99.setKeyStore(is);
			config99.setDevEnv(false);
			config99.setPassword(KSPASSWORD);
			config99.setPoolSize(12);
			config99.setName("IOS99");
			service99= ApnsServiceImpl.createInstance(config99);
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
    	String deviceToken = message.getTo().getResource();
    	
        if(StringUtils.isEmpty(deviceToken)){
        	deviceToken=DeviceToken.get(message.getTo().getNode());
        }
    	// 没有获取到token值，不发生推送消息
        if(StringUtils.isEmpty(deviceToken) || deviceToken.length()<32){
        	return;
        }
		String sendUserName = ContactAction.queryNickeName(sendUser);
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), sendUserName + ":", message.getBody()));
		param.addParam("senderId",sendUser);
		param.addParam("dataType",1);
		param.addParam("dataId",message.getID());
		push(param, deviceToken);
		List<Feedback> listfb =  service99.getFeedbacks();
		if(listfb!=null && !listfb.isEmpty()){
			for(Feedback fb: listfb){
				log.info(fb.getTime()+" @sunshine:ios端卸载设备token("+DeviceToken.getUserNameByToken(fb.getToken())+"="+fb.getToken()+")");
			}
		}
	}
	public static void pushNoticeMessage(final Message message) throws Exception{
		final String msgType;
		final String sendUserName;
		List<String> list=new ArrayList<String>();
		if(message.getType()==Message.Type.headline){
			PacketExtension pkg=message.getExtension("notice", "notice:extension:type");
			if(pkg!=null && StringUtils.isNotEmpty(pkg.getElement().elementTextTrim("type"))){
				msgType=pkg.getElement().elementTextTrim("type");
			}else{
				log.debug("通知类型为空不能推送");
				return;
			}
			JID jid=message.getTo();
			if(null==jid){//全体推送
				sendUserName="";
				List<String> _list=DeviceToken.getAllDeviceTokens();
				if(_list!=null && _list.size()>0){
					list=_list;
				}
			}else{//单个推送
				sendUserName="";
				String deviceToken=DeviceToken.get(jid.getNode());
				if(StringUtils.isEmpty(deviceToken) || deviceToken.length()<32){
					return;
				}
				list.add(deviceToken);
				
			}
			//log.info("@sunshine:apns应推送通知"+list.size()+"条,"+message.getSubject());
			int i=0;
			for(final String str:list){
				ThreadPool.addWork(new QueueTask() {
					@Override
					public void executeTask() throws Exception {
						Payload param=new Payload();
						param.setAlert(alertMessage(message.getSubject(), sendUserName, message.getSubject()));
						param.addParam("senderId",message.getFrom().getNode());
						param.addParam("dataType",msgType);
						param.addParam("dataId",message.getBody());
						push(param, str);
					}
				});
				i++;
			}
			//log.info("@sunshine:apsn实际推送通知"+i+"条,"+message.getSubject());
			clearInvalidToken();
		}
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
		//String groupName = GroupAction.queryGroupName(groupUid);
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), ContactAction.queryNickeName(from)+":", message.getBody()));
		param.addParam("senderId",groupUid+"/"+from);
		param.addParam("dataType",2);//群聊
		param.addParam("dataId",message.getID());
		List<String> itmes = queryGroupMember(groupUid);
		ImCrmPushConfig imCrmPushConfig = new ImCrmPushConfig();
		imCrmPushConfig.setGroupId(groupUid);
		List<ImCrmPushConfig> list= PushConfigAction.findPushConfig(imCrmPushConfig);
		List<String> listUserName= new ArrayList<>();//不走apns推送的用户列表
		for(ImCrmPushConfig _imCrmPushConfig :list){
			listUserName.add(_imCrmPushConfig.getUserName().toLowerCase());
		}
		//log.info("@sunshine:apns应推送群聊(包含没有token的和已屏蔽接受推送的)"+itmes.size()+"条,"+message.getBody());
		int i=0;
		for (String name : itmes) {
			String deviceToken=null;
			if(from.equalsIgnoreCase(name.trim())){
				continue;
			}else {
				deviceToken = DeviceToken.get(name);
			}
			if(StringUtils.isNotEmpty(deviceToken) && !listUserName.contains(name.toLowerCase())){
				i++;
				push(param, deviceToken);
	        }
		}
		//log.info("@sunshine:apns实际推送群聊"+i+"条,"+message.getBody());
		clearInvalidToken();
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
	 * 推送消息
	 * @param param 推送参数
	 * @param deviceToken ios设备deviceToken值
	 */
	private static void push(Payload param, String deviceToken){
		service99.sendNotification(deviceToken, param);
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
			message = "[表情]";
		}else if ("image".equals(subject)) {
			message = "[图片]";
		}else if ("sound".equals(subject)) {
			message = "[语音]";
		}else if ("attachment".equals(subject)) {
			message = "[文件]";
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
		if(service99!=null){
			List<Feedback>  list = service99.getFeedbacks();
			if(list!=null && list.isEmpty()){
				for(Feedback fb:list){
					try {
						IosTokenDao.getInstance().delUserByToken(fb.getToken());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		/**		*/
		String token = 
				       "c258538e942bc449bb78d124a5bf98e29d6da93efec48651bcb41d27effccd7a"//黄
				      + ",fdc8760fd9dc2e17ef338a6c65f8234a8319a8f0781a7f86cbc65111d439c01c"//许
				      + ",d08e660bf425bfa16be88b564a9b77422e16aaf02022f1cd06226cb2a92e0e2d"//王勇
				     //+ ",33b25c77e4cb4844d42905f5f624385d7c7cecdad5cf3dc0a7bb167ba282f56b,"
				      +",f8d4794aa2e7b4c49b2b2dd1baa42fc3c2c2172891670839adc09f5ef9673730"//崔
		               //+ ",193858a0e2e2d62271088aef80ad8ff6bd3ebc92928a9ff897e53b0e3de6d491"
		+ ",0019a78ef1e62005b74ae119fbf30ba3869637fff8cc65f01787edad5aca91e3"//李
		+ ",3eba95fb65a5e2b9f4a6a60b0353531a24cb1da4c38a5aaae1c9228c6ae293d1";//姜
		Payload payload = new Payload();
		payload.setAlert("推送测试");
		payload.setBadge(1);
		payload.addParam("senderId","cc5d916f1ada4a1b84c1f65a969af804/oeg3pw6hs81z8ht2i1hl7ps5kssi");//12,appts
		payload.addParam("dataType","2");//12
		payload.addParam("dataId","5554920f16074f6e9269e4ad3548cd99");
		String[] tokens = token.split("[,]");
		for(String tk:tokens){
			service99.sendNotification(tk, payload);
		}
		service99.remainConnPoolSize();//目前线程池中有多少条线程,总线程数-剩余线程数=正在使用的线程数据
	}
}