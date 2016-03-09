package com.radar.ios;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;
import com.apns.IApnsService;
import com.apns.impl.ApnsServiceImpl;
import com.apns.model.ApnsConfig;
import com.apns.model.Payload;
import com.radar.action.ContactAction;
import com.radar.action.DeviceToken;
import com.radar.action.GroupAction;
import com.radar.action.PushConfigAction;
import com.radar.pool.QueueTask;
import com.radar.pool.ThreadPool;
import com.zyt.web.after.push.remote.ImCrmPushConfig;

/**
 * IOS设备推送消息
 * 分为1对1推送和群聊推送
 * @ClassName:  PushMessage   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年6月30日 上午10:14:27
 */
public class PushMessage {
	private static final int SERVERPORT = 2195;// 服务接口
	private static final String KSTYPE = "PKCS12";
	private static final Pattern pattern = Pattern.compile("[ -]");
	private static final String KSALGORITHM = "SunX509";
	private static final Logger log = LoggerFactory.getLogger(PushMessage.class);

	/**
	 * 环境配置
	 */
	private static String KSPASSWORD="1234";// 证书密码
	private static String SERVERHOST="gateway.push.apple.com";//正式网关;测试网关:gateway.sandbox.push.apple.com
	private static String CERTIFICATE_URL=PushMessage.class.getResource("mom_push_product.p12").getPath();//299证书名称
	private static String CERTIFICATE_URL2=PushMessage.class.getResource("mom_push_product_99.p12").getPath();//99证书名称
	
	public static IApnsService service299;
	public static IApnsService service99;
	static{
		ApnsConfig config299 = new ApnsConfig();
		ApnsConfig config99 = new ApnsConfig();
		try {
			InputStream is = new FileInputStream(CERTIFICATE_URL);
			config299.setKeyStore(is);
			config299.setDevEnv(false);
			config299.setPassword(KSPASSWORD);
			config299.setPoolSize(10);
			config299.setName("IOS299");
			service299= ApnsServiceImpl.createInstance(config299);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			InputStream is = new FileInputStream(CERTIFICATE_URL2);
			config99.setKeyStore(is);
			config99.setDevEnv(false);
			config99.setPassword(KSPASSWORD);
			config99.setPoolSize(10);
			config99.setName("IOS99");
			service99= ApnsServiceImpl.createInstance(config99);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		// 获取用户名称
		String sendUserName = ContactAction.queryNickeName(sendUser);
		/**
		JSONObject param = new JSONObject();
		param.put("alert", alertMessage(message.getSubject(), sendUserName + "：", message.getBody()));
		param.put("sound", "default");
		param.put("badge",1);
		param.put("senderId",sendUser);
		param.put("dataType",1);//单聊
		param.put("dataId",message.getID());
		*/
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), sendUserName + "：", message.getBody()));
		param.addParam("senderId",sendUser);
		param.addParam("dataType",1);
		param.addParam("dataId",message.getID());
		push(param, deviceToken);
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
			if(null==jid){
				sendUserName="";
				List<String> _list=DeviceToken.getAllDeviceTokens();
				if(_list!=null && _list.size()>0){
					list=_list;
				}
			}else{
				sendUserName="";
				String deviceToken=DeviceToken.get(jid.getNode());
				if(StringUtils.isEmpty(deviceToken) || deviceToken.length()<32){
					return;
				}
				list.add(deviceToken);
				
			}
			for(final String str:list){
				ThreadPool.addWork(new QueueTask() {
					@Override
					public void executeTask() throws Exception {
						/**
						JSONObject param = new JSONObject();
						//param.put("alert", alertMessage(message.getSubject(), sendUserName + "：", message.getSubject()));
						param.put("alert", alertMessage(message.getSubject(),"", message.getSubject()));
						param.put("sound", "default");
						param.put("badge",1);
						param.put("senderId",message.getFrom().getNode());
						param.put("dataType",msgType);
						param.put("dataId",message.getBody());
						*/
						Payload param=new Payload();
						param.setAlert(alertMessage(message.getSubject(), sendUserName, message.getSubject()));
						param.addParam("senderId",message.getFrom().getNode());
						param.addParam("dataType",msgType);
						param.addParam("dataId",message.getBody());
						push(param, str);
					}
				});
			}
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
		// 获取群名称
		//String groupName = GroupAction.queryGroupName(groupUid);
        /**
		JSONObject param = new JSONObject();
		param.put("alert", alertMessage(message.getSubject(), ContactAction.queryNickeName(from)+"：", message.getBody()));
		param.put("sound", "default");
		param.put("badge",1);
		param.put("senderId",groupUid+"/"+from);
		param.put("dataType",2);//群聊
		param.put("dataId",message.getID());
		*/
		Payload param=new Payload();
		param.setAlert(alertMessage(message.getSubject(), ContactAction.queryNickeName(from)+"：", message.getBody()));
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
		for (String name : itmes) {
			String deviceToken=null;
			if(from.equalsIgnoreCase(name.trim())){
				continue;
			}else {
				deviceToken = DeviceToken.get(name);
			}
			if(StringUtils.isNotEmpty(deviceToken) && !listUserName.contains(name.toLowerCase())){
				push(param, deviceToken);
	        }
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
	 * 推送消息
	 * @param param 推送参数
	 * @param deviceToken ios设备deviceToken值
	 */
	private static void push(Payload param, String deviceToken){
		service299.sendNotification(deviceToken, param);
		service99.sendNotification(deviceToken, param);
		/*
		JSONObject result = new JSONObject();
		result.put("aps", param);
		result.put("cpn", (new JSONObject()).put("t0", System.currentTimeMillis()));
		byte[] msgByte = makebyte((byte) 1, deviceToken, result.toString(), 10000001);
			Socket socket=null;
			try {
				socket=getSocket();
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(msgByte);
				outputStream.close();
				log.debug("ios推送299:"+deviceToken+":"+param.toJSONString());
			} catch (Exception e) {
				e.printStackTrace();
				log.info("IOS推送失败299:"+e.getMessage());
			}finally{
				try {
					socket.close();
				} catch (IOException e) {
					log.error("socket299:"+e.getMessage());
				}
			}
		
		   try {
			    socket=getSocket2();
				OutputStream outputStream2 = socket.getOutputStream();
				outputStream2.write(msgByte);
				outputStream2.close();
				log.debug("ios推送99:"+deviceToken+":"+param.toJSONString());
		    } catch (Exception e) {
				e.printStackTrace();
				log.info("IOS推送失败99:"+e.getMessage());
			}finally{
				try {
					socket.close();
				} catch (IOException e) {
					log.error("socket99:"+e.getMessage());
				} 
			}
		   */
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
	 * 获取apple推送服务器的socket
	 * 如果socket为空或者socket已经关闭则重新连接
	 * @return socket
	 * @throws Exception
	 */
	@Deprecated
	private static Socket getSocket() throws Exception{
		
			InputStream certInput = new FileInputStream(CERTIFICATE_URL);
			
			KeyStore keyStore = KeyStore.getInstance(KSTYPE);
			keyStore.load(certInput, KSPASSWORD.toCharArray());
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KSALGORITHM);
			kmf.init(keyStore, KSPASSWORD.toCharArray());
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, null);
			SSLSocketFactory socketFactory = sslContext.getSocketFactory();
			Socket socket = socketFactory.createSocket(SERVERHOST, SERVERPORT);
		    return socket;
	}	
	/**
	 * 组装apns规定的字节数组 使用增强型 *
	 * @param command
	 * @param deviceToken
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	private static byte[] makebyte(byte command, String deviceToken,String payload, int identifer) {
		byte[] deviceTokenb = decodeHex(deviceToken);
		byte[] payloadBytes = null;
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(boas);
		try {
			payloadBytes = payload.getBytes("UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		try {
			dos.writeByte(command);
			dos.writeInt(identifer);
			dos.writeInt(Integer.MAX_VALUE);
			dos.writeShort(deviceTokenb.length);
			dos.write(deviceTokenb);
			dos.writeShort(payloadBytes.length);
			dos.write(payloadBytes);
			return boas.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	private static byte[] decodeHex(String deviceToken) {
		String hex = pattern.matcher(deviceToken).replaceAll("");
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) (charval(hex.charAt(2 * i)) * 16 + charval(hex
					.charAt(2 * i + 1)));
		}
		return bts;
	}

	private static int charval(char a) {
		if ('0' <= a && a <= '9')
			return (a - '0');
		else if ('a' <= a && a <= 'f')
			return (a - 'a') + 10;
		else if ('A' <= a && a <= 'F')
			return (a - 'A') + 10;
		else {
			throw new RuntimeException("Invalid hex character: " + a);
		}
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
}