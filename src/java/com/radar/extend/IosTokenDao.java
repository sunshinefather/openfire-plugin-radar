package com.radar.extend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IosTokenDao {
    private static final Logger log = LoggerFactory.getLogger(IosTokenDao.class);
    
    /**
     * 查询所有设备token
     */
    public static final String INSERT_OFDEVICETOKEN ="INSERT INTO ofdevicetoken (userName, iosToken) VALUES (?,?)";
    
    /**
     * 查询所有设备token
     */
    public static final String QUERY_ALL_IOSTOKEN ="SELECT userName,iosToken FROM ofdevicetoken";
    
    /**
     * 根据用户查询设备token
     */
    public static final String QUERY_IOSTOKEN_BYUSERNAME ="SELECT userName,iosToken FROM ofdevicetoken WHERE userName=?";
    
    /**
     * 根据设备token查询用户
     */
    public static final String QUERY_USERNAME_BYIOSTOKEN ="SELECT userName,iosToken FROM ofdevicetoken WHERE iosToken=?";
    
    /**
     * 根据设备token查询用户
     */
    public static final String DELETE_USERNAME_BYIOSTOKEN ="DELETE FROM ofdevicetoken WHERE iosToken=?";
    
    /**
     * 根据设备token查询用户
     */
    public static final String DELETE_USERNAME_BYUSERNAME ="DELETE FROM ofdevicetoken WHERE userName=?";
    
    private static IosTokenDao iostokenDao =new IosTokenDao();
    
    private IosTokenDao(){
    };
    
    public static IosTokenDao getInstance(){
    	return iostokenDao;
    }
    /**
     * 查询所有用户
     * @Title: getAllUser
     * @Description: TODO  
     * @param: @return
     * @param: @throws SQLException      
     * @return: Map<String,String>
     * @author: sunshine  
     * @throws
     */
    public Map<String,String> getAllUser() throws SQLException
    {
        Map<String,String> userMap =new HashMap<String,String>();
        Connection conn = DbConnectionManager.getConnection();
        PreparedStatement pst = conn.prepareStatement(QUERY_ALL_IOSTOKEN);
        ResultSet rs = pst.executeQuery();
        while (rs.next())
        {
            String userName = rs.getString("userName");
            String iosToken = rs.getString("iosToken");
            userMap.put(iosToken, userName);
        }
        return userMap;
    }
    /**
     * 根据token查询用户
     * @Title: getUserNameByToken
     * @Description: TODO  
     * @param: @param token
     * @param: @return
     * @param: @throws SQLException      
     * @return: Map<String,String>
     * @author: sunshine  
     * @throws
     */
    public String getUserNameByToken(String token) throws SQLException
    {
    	Connection conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(QUERY_USERNAME_BYIOSTOKEN);
    	pst.setString(1, token);
    	ResultSet rs = pst.executeQuery();
    	rs.next();
		String userName = rs.getString("userName");
		return userName;
    }
    /**
     * 根据用户查询token
     * @Title: getTokenByUserName
     * @Description: TODO  
     * @param: @param userName
     * @param: @return
     * @param: @throws SQLException      
     * @return: String
     * @author: sunshine  
     * @throws
     */
    public String getTokenByUserName(String userName) throws SQLException
    {
    	Connection conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(QUERY_IOSTOKEN_BYUSERNAME);
    	pst.setString(1, userName);
    	ResultSet rs = pst.executeQuery();
    	rs.next();
    	String iosToken = rs.getString("iosToken");
    	return iosToken;

    }
    
    public boolean delUserByToken(String token) throws SQLException
    {
    	Connection conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYIOSTOKEN);
    	pst.setString(1, token);
    	int rs = pst.executeUpdate();
    	if(rs>0){
    		return true;
    	}else{
    		log.error("删除用户设备失败");
    	}
    	return false;
    }
    
    public boolean delTokenByUser(String userName) throws SQLException
    {
    	Connection conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYUSERNAME);
    	pst.setString(1, userName);
    	int rs = pst.executeUpdate();
    	if(rs>0){
    		return true;
    	}else{
    		log.error("删除用户设备失败");
    	}
    	return false;
    }
    
    public boolean saveIosToken(String userName,String token) throws SQLException
    {
    	Connection conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(INSERT_OFDEVICETOKEN);
    	pst.setString(1, userName);
    	pst.setString(2, token);
    	int rs = pst.executeUpdate();
    	if(rs>0){
    		return true;
    	}else{
    		log.error("保存用户设备失败");	
    	}
    	return false;
    }
}