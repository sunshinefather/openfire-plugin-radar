package com.radar.extend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.SequenceManager;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.JiveConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
/**
 * 
 * @ClassName:  OfflineDao   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月30日 下午4:43:27
 */
public class OfflineDao
{
    private static final Logger Log = LoggerFactory.getLogger(OfflineDao.class);
    
    /**插入离线消息*/
    public static final String INSERT_OFFLINE ="INSERT INTO ofOffline (username, messageID, creationDate, messageSize, stanza) VALUES (?, ?, ?, ?, ?)";
   
    /**根据用户名和消息id删除离线消息*/
    public static final String DELETE_OFFLINE_MESSAGE ="DELETE FROM ofOffline WHERE username=? AND messageID=?";
    
    /**根据消息id查询消息*/
    public static final String QUERY_OFFINE_SIZE ="SELECT messageSize FROM ofOffline WHERE messageID=?";
    
    /**根据用户名删除离线群消息 */
    public static final String DELETE_OFFLIEMESSAGE = "DELETE FROM ofOffline WHERE username=? AND stanza LIKE '<message type=\"groupchat\"%' ORDER BY creationDate LIMIT 300";
    
    /**统计用户群离线消息数量 */
    public static final String QUERY_OFFLIEMESSAGE = "SELECT COUNT(1) FROM ofOffline WHERE username=? AND stanza LIKE '<message type=\"groupchat\"%'";
    
    private static OfflineDao offlineDao =new OfflineDao();
    
    private OfflineDao(){
    	throw new IllegalAccessError("非法访问");
    };
    
    public static  OfflineDao getInstance(){
    	return offlineDao;
    };
    /**
     * 添加消息内容到离线表
     * @param message 内容
     */
    public void addMessage(Message message) {
        if (message == null) {
            return;
        }
        JID recipient = message.getTo();
        if(recipient==null){
        	return;
        }
        String username = recipient.getNode();

        long messageID = SequenceManager.nextID(JiveConstants.OFFLINE);
        
        message.setID(String.valueOf(messageID));

        String msgXML = message.getElement().asXML();

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(INSERT_OFFLINE);
            pstmt.setString(1, username);
            pstmt.setLong(2, messageID);
            pstmt.setString(3, System.currentTimeMillis()+"");
            pstmt.setInt(4, msgXML.length());
            pstmt.setString(5, msgXML);
            int result = pstmt.executeUpdate();
            if(result<1)
                Log.error("保存离线消息内容失败："+message.toXML());
        }

        catch (Exception e) {
            Log.error("保存离线消息内容失败："+message.toXML(), e);
        }
        finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
    }
    /**
     * 获取离线消息内容长度
     * @param messageId 消息id
     * @return 内容长度
     */
    public  int getMessageSize(Long messageId) {
        if (messageId == null) {
            return 0;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(QUERY_OFFINE_SIZE);
            pstmt.setLong(1, messageId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        }catch (Exception e) {
            Log.error("获取离线消息内容大小失败", e);
        }
        finally {
            DbConnectionManager.closeConnection(rs,pstmt, con);
        }
        return 0;
    }
    /**
     * 删除 离线消息  
     * @param messageId  消息id
     * @param username   用户名
     */
    public void delMessage(Long  messageId,String username) {
        if (messageId == null || username == null) {
            return;
        }
        
        if (!UserManager.getInstance().isRegisteredUser(username)) {
            return;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_OFFLINE_MESSAGE);
            pstmt.setString(1, username);
            pstmt.setLong(2, Long.valueOf(messageId));
            int result = pstmt.executeUpdate();
            if(result<1)
                Log.info("删除离线消息{"+messageId+"},{"+username+"}");
        }catch (Exception e) {
            Log.error("删除离线消息{"+messageId+"},{"+username+"}");
        }finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
    }
    
    /**
     * 根据用户名查询群离线消息数量
     * @param userName
     * @return
     */
    public int queryCount(String userName){
    	if (userName == null || "".equals(userName)) {
            return 0;
        }
    	
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(QUERY_OFFLIEMESSAGE);
            pstmt.setString(1, userName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        }catch (Exception e) {
            Log.error("更加用户名获取离线消息内容数量异常：" + e.getMessage());
        }
        finally {
            DbConnectionManager.closeConnection(rs,pstmt, con);
        }
        return 0;
    }

    /**
     * 根据用户名删除群离线消息，删除前300条离线消息
     * @param userName 用户名
     */
    public void deleteOfflineMessage(String userName){
    	if(userName == null || "".equals(userName)){
    		return;
    	}
    	
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_OFFLIEMESSAGE);
            pstmt.setString(1, userName);
            int result = pstmt.executeUpdate();
            if(result>0){
            	Log.info("删除过量离线消息"+result+"条，用户名："+userName);
            }else {
            	Log.info("删除过量离线消息失败，用户名："+userName);
			}
        }catch (Exception e) {
        	Log.error("删除过量离线消息异常，用户名："+userName);
        }finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
    }
}