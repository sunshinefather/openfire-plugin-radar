package com.radar.common;
public class SqlConstant
{

    /**
     * 修改openfire用户名密码
     */
    public static final String UPDATE_PASSWORD = "UPDATE ofUser SET plainPassword=?, encryptedPassword=? WHERE username=?";

    /**
     * 查询所有的用户名和密文
     */
    public static final String LOAD_ALL_USER = "SELECT username,encryptedPassword,creationDate,modificationDate FROM ofUser";

    /**
     * openfire用户insert sql语句
     */
    public static final String INSERT_USER = "INSERT INTO ofUser (username,plainPassword,encryptedPassword,creationDate,modificationDate) "
            + "VALUES (?,?,?,?,?)";

    /**
     * 根据用户名加载openfire用户信息
     */
    public static final String LOAD_USER = "SELECT encryptedPassword,creationDate, modificationDate FROM ofUser WHERE username=?";
    
    /**
     * 根据用户名删除离线消息
     */
    
    public static final String DELETE_OFFLIEMESSAGE = "DELETE FROM ofOffline WHERE username=? AND stanza LIKE '<message type=\"groupchat\"%' ORDER BY creationDate LIMIT 300";
    
    /**
     * 统计用户群离线消息数量
     */
    public static final String QUERY_OFFLIEMESSAGE = "SELECT COUNT(1) FROM ofOffline WHERE username=? AND stanza LIKE '<message type=\"groupchat\"%'";
}
