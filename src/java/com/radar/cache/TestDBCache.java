package com.radar.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.cache.Cache;
import org.jivesoftware.util.cache.CacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;

public class TestDBCache {
	  private static TestDBCache TestDBCache = new TestDBCache();
	  private static Thread writeThread = null;
	  private static final String MESSAGE_SEPARATOR = ",";
	  private static final String PARAMETER_SEPARATOR = "、";
	  private static ICacheService cache = new TestCacheService();
	  private static final String TestDBCache_BASE = "message_cache";
	  private static final String TestDBCache_USER = "message_cache_";
	  private static final Long WAIT_TIME = Long.valueOf(3000L);

	  private static final Long WAIT_LIGHT_TIME = Long.valueOf(2000L);

	  private static final Long DELETE_TIME = Long.valueOf(3000L);

	  private static boolean DELETE_FLAG = false;
	  private static final Logger log = LoggerFactory.getLogger(TestDBCache.getClass());

	  private static Cache<String, Integer> offlineMessageNumCache = CacheFactory.createCache("Offline Message number");
	  private static final String INSERT_OFFLINE = "INSERT INTO ofOffline (username, messageID, creationDate, messageSize, stanza) VALUES (?, ?, ?, ?, ?)";
	  private static final String LOAD_OFFLINE_MESSAGE = "SELECT stanza FROM ofOffline WHERE username=? AND creationDate=?";
	  private static final String DELETE_OFFLINE_MESSAGE = "DELETE FROM ofOffline WHERE username=? AND messageID=?";
	  private static final String DELETE_USER_OFFLINE_MESSAGE = "DELETE FROM ofOffline WHERE username=?";
	  private static final String LOAD_OFFLINE = "SELECT messageID, stanza FROM ofOffline WHERE username=?";
	  private static final String SELECT_NUM_OFFLINE = "SELECT COUNT(1) FROM ofOffline WHERE username=?";

	  private TestDBCache()
	  {
	    new writeToDB();
	  }

	  public static void addMessageNum(Message message)
	  {
	    String messageID = message.getID();
	    if (StringUtils.isEmpty(messageID)) {
	      message.setID(String.valueOf(System.currentTimeMillis()));
	      messageID = message.getID();
	    }

	    StringBuffer sBMessage = new StringBuffer();
	    sBMessage.append(message.toXML());
	    sBMessage.append(MESSAGE_SEPARATOR);
	    sBMessage.append(messageID);
	    sBMessage.append(PARAMETER_SEPARATOR);
	    sBMessage.append(message.getTo().getNode());

	    cache.setMap("message_cache", message.getTo().getNode(), "message_cache_" + message.getTo().getNode());
	    cache.setMap("message_cache_" + message.getTo().getNode(), messageID, sBMessage.toString());

	    if (offlineMessageNumCache.containsKey(message.getTo().getNode()))
	      offlineMessageNumCache.put(message.getTo().getNode(), Integer.valueOf(offlineMessageNumCache.get(message.getTo().getNode()).intValue() + 1));
	  }

	  public static Long removeMessage(String messageID, String username)
	  {
	    Long result = removeCacheMessage(messageID, username);
	    if (result.longValue() == 0L) {
	      result = Long.valueOf(deleteDBMessage(messageID, username));
	    }

	    if (result.longValue() != 0L) {
	      offlineMessageNumCache.put(username, Integer.valueOf(offlineMessageNumCache.get(username).intValue() - 1));
	    }

	    return result;
	  }

	  public static Long removeCacheMessage(String messageID, String username)
	  {
	    return cache.removeMap("message_cache_" + username, messageID);
	  }

	  public static void removeUserMessage(String username)
	  {
	    DELETE_FLAG = true;
	    cache.removeMap("message_cache", username);
	    cache.remove("message_cache_" + username);
	    deleteDBMessage(username);

	    if (offlineMessageNumCache.containsKey(username))
	      offlineMessageNumCache.remove(username);
	  }

	  public static TreeMap<String, String> getMessages(String username)
	  {
	    TreeMap msgMap = new TreeMap();
	    List<String> list = cache.getMapValue("message_cache_" + username, String.class);

	    if ((list != null) && (list.size() > 0)) {
	      for (String msg : list) {
	        int index = msg.lastIndexOf(",");
	        String msgXML = msg.substring(0, index);
	        String[] msgInfo = msg.substring(index + 1).split("、");
	        msgMap.put(msgInfo[0], msgXML);
	      }

	    }

	    msgMap.putAll(getDBMessages(username));

	    return msgMap;
	  }

	  public static String getMessage(String username, Date createDate)
	  {
	    String message = (String)cache.get("message_cache_" + username, String.valueOf(createDate.getTime()), String.class);

	    if ((message == null) || ("".equals(message))) {
	      message = getDBMessage(username, createDate);
	    }

	    return message;
	  }

	  public static TreeMap<String, String> getCacheMessages(String key)
	  {
	    TreeMap msgMap = null;
	    List<String> list = cache.getMapValue(key, String.class);

	    if ((list != null) && (list.size() > 0)) {
	      msgMap = new TreeMap();
	      for (String msg : list) {
	        int index = msg.lastIndexOf(",");
	        String[] msgInfo = msg.substring(index + 1).split("、");
	        msgMap.put(msgInfo[0], msg);
	      }
	    }

	    return msgMap;
	  }

	  public static int addDBMessage(String messageID, String username, String message)
	  {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    try {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(INSERT_OFFLINE);
	      pstmt.setString(1, username);
	      pstmt.setLong(2, Long.valueOf(messageID).longValue());
	      pstmt.setString(3, messageID);
	      pstmt.setInt(4, message.length());
	      pstmt.setString(5, transitionChat(message));

	      return pstmt.executeUpdate();
	    }
	    catch (Exception e) {
	      log.error("保存离线消息内容失败：" + message, e);

	      return 0;
	    }
	    finally
	    {
	      DbConnectionManager.closeConnection(pstmt, con);
	    }
	  }

	  public static int deleteDBMessage(String messageId, String username)
	  {
	    if ((messageId == null) || (username == null) || (!UserManager.getInstance().isRegisteredUser(username)))
	    {
	      return -1;
	    }

	    Connection con = null;
	    PreparedStatement pstmt = null;
	    try {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(DELETE_OFFLINE_MESSAGE);
	      pstmt.setString(1, username);
	      pstmt.setString(2, messageId);

	      return pstmt.executeUpdate();
	    } catch (Exception e) {
	      log.error("删除离线消息{" + messageId + "},{" + username + "}");

	      return -1;
	    } finally {
	      DbConnectionManager.closeConnection(pstmt, con);
	    }
	  }

	  public static int deleteDBMessage(String username)
	  {
	    if ((username == null) || (!UserManager.getInstance().isRegisteredUser(username))) {
	      return -1;
	    }

	    Connection con = null;
	    PreparedStatement pstmt = null;
	    try {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(DELETE_USER_OFFLINE_MESSAGE);
	      pstmt.setString(1, username);

	      return pstmt.executeUpdate();
	    } catch (Exception e) {
	      log.error(username + "删除离线消息异常：" + e.getMessage());

	      return -1;
	    } finally {
	      DbConnectionManager.closeConnection(pstmt, con);
	    }
	  }

	  public static TreeMap<String, String> getDBMessages(String username)
	  {
	    TreeMap msgMap = new TreeMap();
	    if (username == null) {
	      return msgMap;
	    }

	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try
	    {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(LOAD_OFFLINE);
	      pstmt.setString(1, username);

	      rs = pstmt.executeQuery();
	      while (rs.next())
	        msgMap.put(rs.getString(1), rs.getString(2));
	    }
	    catch (Exception e) {
	      log.error(username + "获取离线消息内容失败:", e.getMessage());

	      return msgMap;
	    }
	    finally {
	      DbConnectionManager.closeConnection(rs, pstmt, con);
	    }

	    return msgMap;
	  }

	  public static String getDBMessage(String username, Date createDate)
	  {
	    ResultSet rs = null;
	    String message = null;
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    try
	    {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(LOAD_OFFLINE_MESSAGE);
	      pstmt.setString(1, username);
	      pstmt.setString(2, String.valueOf(createDate.getTime()));

	      rs = pstmt.executeQuery();
	      while (rs.next())
	        message = rs.getString(1);
	    }
	    catch (Exception e) {
	      log.error(username + "获取单条离线消息内容失败:", e.getMessage());

	      return message;
	    }
	    finally {
	      DbConnectionManager.closeConnection(rs, pstmt, con);
	    }

	    return message;
	  }
      /**
       * 获取离线消息条数
       * @Title: getOfflineMessageNum
       * @Description: TODO  
       * @param: @param username
       * @param: @return      
       * @return: int
       * @author: sunshine  
       * @throws
       */
	  public static int getOfflineMessageNum(String username)
	  {
	    if (offlineMessageNumCache.containsKey(username)) {
	      return ((Integer)offlineMessageNumCache.get(username)).intValue();
	    }

	    int size = 0;
	    TreeMap maps = getMessages(username);
	    if (maps != null) {
	      size = maps.size();
	    }

	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	      con = DbConnectionManager.getConnection();
	      pstmt = con.prepareStatement(SELECT_NUM_OFFLINE);
	      pstmt.setString(1, username);
	      rs = pstmt.executeQuery();
	      if (rs.next()) {
	        size += rs.getInt(1);
	      }

	      offlineMessageNumCache.put(username, Integer.valueOf(size));
	    }
	    catch (Exception e) {
	      log.error(LocaleUtils.getLocalizedString("admin.error"), e);
	    }
	    finally {
	      DbConnectionManager.closeConnection(rs, pstmt, con);
	    }
	    return size;
	  }

	  public static String transitionChat(String string)
	  {
	    try
	    {
	      StringBuffer sBuffer = new StringBuffer();
	      int i1 = string.indexOf("<body>") + 6;
	      int i2 = string.indexOf("</body>");
	      int i3 = string.length();

	      sBuffer.append(string.substring(0, i1));
	      char[] chars = string.substring(i1, i2).toCharArray();
	      sBuffer.append(new String(chars));
	      sBuffer.append(string.substring(i2, i3));

	      return sBuffer.toString();
	    }
	    catch (Exception e) {
	    }
	    return string;
	  }

	  private class writeToDB
	  {
	    private writeToDB()
	    {
	      TestDBCache.writeThread=new Thread(new Runnable()
	      {
	        public void run()
	        {
	          while (true)
	            try
	            {
	              List list = TestDBCache.cache.getMapValue("message_cache", String.class);

	              if (list != null) {
	                Iterator it = list.iterator(); if (it.hasNext()) { String cacheKey = (String)it.next();
	                  if (TestDBCache.DELETE_FLAG) {
	                    TestDBCache.DELETE_FLAG=false;
	                    continue;
	                  }

	                  TestDBCache.writeToDB.this.handler(cacheKey);
	                  continue;
	                }
	              }
	              if (!TestDBCache.DELETE_FLAG)
	                Thread.sleep(TestDBCache.WAIT_TIME.longValue());
	              else
	                Thread.sleep(TestDBCache.WAIT_LIGHT_TIME.longValue());
	            }
	            catch (Exception e) {
	              TestDBCache.log.debug("Message从Redis写入数据库异常：" + e.getMessage());
	              e.printStackTrace();
	            }
	        }
	      });
	      TestDBCache.writeThread.start();
	    }

	    private void handler(String key)
	    {
	      TreeMap entrys = TestDBCache.getCacheMessages(key);

	      if (entrys == null) {
	        return;
	      }

	      for (Object entry : entrys.entrySet()) {
	        if (TestDBCache.DELETE_FLAG) {
	          return;
	        }

	        String msg = (String)((Map.Entry)entry).getValue();

	        int index = msg.lastIndexOf(",");
	        String msgXML = msg.substring(0, index);
	        String[] msgInfo = msg.substring(index + 1).split("、");
	        Long jetTime = Long.valueOf(System.currentTimeMillis() - Long.valueOf(msgInfo[0]).longValue());

	        if (jetTime.longValue() > TestDBCache.DELETE_TIME.longValue())
	        {
	          int deleteNum = TestDBCache.addDBMessage(msgInfo[0], msgInfo[1], msgXML);

	          if (deleteNum != 0)
	            TestDBCache.removeCacheMessage(msgInfo[0], msgInfo[1]);
	        }
	      }
	    }
	  }
}