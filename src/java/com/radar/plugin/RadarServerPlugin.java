package com.radar.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.component.InternalComponentManager;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.StringUtils;
import org.xmpp.component.ComponentException;

import com.radar.common.EnvConstant;
import com.radar.component.ReceiptComponent;
import com.radar.hander.contact.ContactAddFriendsGroupIQHander;
import com.radar.hander.contact.ContactAddMemberIQHander;
import com.radar.hander.contact.ContactDelMemberIQHander;
import com.radar.hander.contact.ContactDellFriendsGroupIQHander;
import com.radar.hander.contact.ContactListIQHander;
import com.radar.hander.contact.ContactMoveMemberIQHander;
import com.radar.hander.contact.ContactReNameFriendsGroupIQHander;
import com.radar.hander.contact.ContactReNameMemberIQHander;
import com.radar.hander.deviceTokenAuth.AddDeviceTokenHandler;
import com.radar.hander.deviceTokenAuth.RemoveDeviceTokenHandler;
import com.radar.hander.dialogue.DialogueDelIQHander;
import com.radar.hander.dialogue.DialogueListIQHander;
import com.radar.hander.groupchat.GroupRoomAddMemberIQHander;
import com.radar.hander.groupchat.GroupRoomByIdIQHander;
import com.radar.hander.groupchat.GroupRoomCreateIQHander;
import com.radar.hander.groupchat.GroupRoomListIQHander;
import com.radar.hander.groupchat.GroupRoomMemberListIQHander;
import com.radar.hander.groupchat.GroupRoomMembersIQHander;
import com.radar.hander.groupchat.GroupRoomMergeIQHander;
import com.radar.hander.groupchat.GroupRoomRemoveMemberIQHander;
import com.radar.hander.message.MessageDelIQHander;
import com.radar.hander.message.MessageListIQHander;
import com.radar.hander.notice.NoticeAccepterListIQHander;
import com.radar.hander.notice.NoticeByIdIQHander;
import com.radar.hander.notice.NoticeSenderListIQHander;
import com.radar.hander.notice.NoticeUpdateStateIQHander;
import com.radar.hander.notice.SendNoticeIQHander;
import com.radar.hander.pushconfig.PushConfigStatusIQHander;
import com.radar.hander.pushconfig.PushConfigSwitchIQHander;
import com.radar.interceptor.IQInterceptor;
import com.radar.interceptor.SearchEngineInterceptor;
import com.radar.listener.PropertyListenerImpl;
import com.radar.pool.ThreadPool;
import com.radar.utils.HixinUtils;

public class RadarServerPlugin implements Plugin
{
    /**
     * XMPP协议
     */
    private XMPPServer server;
    private static PluginManager pluginManager;
    private InterceptorManager interceptorManager;
    private InternalComponentManager internalComponentManager ;
    private PropertyListenerImpl  propertyEventListener = new PropertyListenerImpl();//配置文件监听器
    private ReceiptComponent receiptComponent = new ReceiptComponent();//消息回执组件
    private IQInterceptor iqInterceptor = new IQInterceptor();//自定义拦截器(移除ISOToken)
    private SearchEngineInterceptor searchEngineInterceptor = new SearchEngineInterceptor();//单聊，群聊，保存聊天记录
    private List<IQHandler> listIQ=new ArrayList<IQHandler>();
    
    public RadarServerPlugin() {         
        interceptorManager = InterceptorManager.getInstance();
        internalComponentManager = InternalComponentManager.getInstance();
    }
    
    /**
     * 插件初始化
     */
    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory)
    {
        server = XMPPServer.getInstance();
        pluginManager = manager;
        initCustomIQ();
        initCustomConstant();
        //加入监听
        PropertyEventDispatcher.addListener(propertyEventListener);
    
        interceptorManager.addInterceptor(iqInterceptor);
        interceptorManager.addInterceptor(searchEngineInterceptor);
        
        try {
            internalComponentManager.addComponent(HixinUtils.getReceiptName(), receiptComponent);
        } catch (ComponentException e1) {
            e1.printStackTrace();
        }
        
        initOpenfireExtend();
    }
    
    private void initCustomIQ(){
        /**联系人IQ*/
        listIQ.add(new ContactListIQHander());
        listIQ.add(new ContactAddFriendsGroupIQHander());
        listIQ.add(new ContactAddMemberIQHander());
        listIQ.add(new ContactDellFriendsGroupIQHander());
        listIQ.add(new ContactDelMemberIQHander());
        listIQ.add(new ContactMoveMemberIQHander());
        listIQ.add(new ContactReNameFriendsGroupIQHander());
        listIQ.add(new ContactReNameMemberIQHander());
        
        /**群组房间处理*/
        listIQ.add(new GroupRoomAddMemberIQHander());
        listIQ.add(new GroupRoomByIdIQHander());
        listIQ.add(new GroupRoomCreateIQHander());
        listIQ.add(new GroupRoomListIQHander());
        listIQ.add(new GroupRoomMembersIQHander());
        listIQ.add(new GroupRoomMemberListIQHander());
        listIQ.add(new GroupRoomMergeIQHander());
        listIQ.add(new GroupRoomRemoveMemberIQHander());
        
        /**聊天记录处理*/
        listIQ.add(new MessageListIQHander());
        listIQ.add(new MessageDelIQHander());
        
        /**会话处理*/
        listIQ.add(new DialogueListIQHander());
        listIQ.add(new DialogueDelIQHander());
        
        /**推送通知*/
        listIQ.add(new SendNoticeIQHander());
        listIQ.add(new NoticeAccepterListIQHander());
        listIQ.add(new NoticeSenderListIQHander());
        listIQ.add(new NoticeByIdIQHander());
        listIQ.add(new NoticeUpdateStateIQHander());
        
        /**ios处理*/
        listIQ.add(new RemoveDeviceTokenHandler());
        listIQ.add(new AddDeviceTokenHandler());
        
        /**群apns推送配置*/
        listIQ.add(new PushConfigSwitchIQHander());
        listIQ.add(new PushConfigStatusIQHander());
        
        for(IQHandler iq:listIQ){
        	server.getIQRouter().addHandler(iq);
        }
    }
    
    private void initCustomConstant(){
    	
        EnvConstant.SECRET = JiveGlobals.getProperty("plugin.radar.secret",StringUtils.randomString(8));
        
        //允许的IP集合 默认为空
        EnvConstant.ALLOWEDIPS = StringUtils.stringToCollection(JiveGlobals.getProperty("plugin.radar.allowedIPs", ""));
       
        //搜索引擎访问地址
        EnvConstant.SEARCH_ENGINESURL = JiveGlobals.getProperty("plugin.radar.searchEnginesUrl","");
       
        //memcached访问地址
        EnvConstant.MEMCACHED_ADDRESS = JiveGlobals.getProperty("plugin.radar.memcachedAddress","");
        
        EnvConstant.KSPASSWORD=JiveGlobals.getProperty("plugin.iospush.password","1234");
        
        EnvConstant.IS_PRODUCT_ENV=JiveGlobals.getBooleanProperty("plugin.iospush.model");
        
    }
    
    /**
     * 初始化openfire数据扩展类
     */
    private void initOpenfireExtend()
    {  
/*common   	
        JiveGlobals.setProperty("provider.auth.className", "org.jivesoftware.openfire.auth.JDBCAuthProvider");
        JiveGlobals.setProperty("provider.user.className", "org.jivesoftware.openfire.user.JDBCUserProvider");
        //支持plain与(md5,sha1,sha256,sha512这四种存储为十六进制编码)
        JiveGlobals.setProperty("jdbcAuthProvider.passwordType","plain");
        //允许登陆控制台的账户,多个用','逗号分割
        JiveGlobals.setProperty("admin.authorizedJIDs","admin@localhost");
        JiveGlobals.setProperty("sasl.mechs","ANONYMOUS,PLAIN,DIGEST-MD5,CRAM-MD5,JIVE-SHAREDSECRET");
        
*/         
        //JiveGlobals.setProperty("jdbcAuthProvider.setPasswordSQL","");
        //JiveGlobals.setProperty("jdbcAuthProvider.allowUpdate","");
        //JiveGlobals.setProperty("provider.group.className", "org.jivesoftware.openfire.group.JDBCGroupProvider");
        
/*mysql 
        JiveGlobals.setProperty("jdbcProvider.driver","com.mysql.jdbc.Driver");
        JiveGlobals.setProperty("jdbcProvider.connectionString","jdbc:mysql://192.168.253.233:3306/mom_user_platform?user=root&password=1qa2ws3ed&useUnicode=true&characterEncoding=utf8");
        JiveGlobals.setProperty("jdbcAuthProvider.passwordSQL","SELECT password FROM sys_user WHERE user_name=?");
        JiveGlobals.setProperty("jdbcUserProvider.loadUserSQL","SELECT user_name,email FROM sys_user a join sys_ext_user b on a.id=b.id WHERE user_name=?");
        JiveGlobals.setProperty("jdbcUserProvider.userCountSQL","select count(0) from sys_user");
        JiveGlobals.setProperty("jdbcUserProvider.allUsersSQL","select user_name from sys_user");
        JiveGlobals.setProperty("jdbcUserProvider.searchSQL","SELECT user_name FROM sys_user a join sys_ext_user b on a.id=b.id WHERE ");
        JiveGlobals.setProperty("jdbcUserProvider.usernameField","user_name");
        JiveGlobals.setProperty("jdbcUserProvider.nameField","user_name");
        JiveGlobals.setProperty("jdbcUserProvider.emailField","email"); 
*/         
/* pgsql        
        JiveGlobals.setProperty("jdbcProvider.driver","org.postgresql.Driver");
        JiveGlobals.setProperty("jdbcProvider.connectionString","jdbc:postgresql://192.168.253.241:5432/child_spring?user=postgres&password=1234&useUnicode=true&characterEncoding=utf8");
        JiveGlobals.setProperty("jdbcAuthProvider.passwordSQL","SELECT DISTINCT password FROM v_user where id=?::numeric");
        JiveGlobals.setProperty("jdbcUserProvider.loadUserSQL","SELECT name,username as email FROM v_user WHERE id=?::numeric");
        JiveGlobals.setProperty("jdbcUserProvider.userCountSQL","select count(DISTINCT id) from v_user");
        JiveGlobals.setProperty("jdbcUserProvider.allUsersSQL","select DISTINCT id from v_user");
        JiveGlobals.setProperty("jdbcUserProvider.searchSQL","select DISTINCT id from v_user WHERE ");
        JiveGlobals.setProperty("jdbcUserProvider.usernameField","id");
        JiveGlobals.setProperty("jdbcUserProvider.nameField","name");
        JiveGlobals.setProperty("jdbcUserProvider.emailField","email");
        JiveGlobals.setProperty("admin.authorizedJIDs","488@127.0.0.1");
*/
    }

    
    /**
     * 插件销毁
     */
    @Override
    public void destroyPlugin()
    {
        pluginManager = null;
        //移出 销毁
        PropertyEventDispatcher.removeListener(propertyEventListener);
        interceptorManager.removeInterceptor(iqInterceptor); 
        interceptorManager.removeInterceptor(searchEngineInterceptor);
        
        internalComponentManager.removeComponent(HixinUtils.getReceiptName(), receiptComponent);
        if(listIQ!=null && !listIQ.isEmpty()){
            for(IQHandler iq:listIQ){
            	server.getIQRouter().removeHandler(iq);
            }
            listIQ=null;
        }
        ThreadPool.shutdown();  //销毁线程池
    }

    //get set方法
    public XMPPServer getServer()
    {
        return server;
    }

    public void setServer(XMPPServer server)
    {
        this.server = server;
    }
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
}
