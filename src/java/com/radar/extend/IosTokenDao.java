package com.radar.extend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * @return: Map<String,String>
     * @author: sunshine  
     * @throws
     */
    public Map<String,String> getAllUser()
    {
        Map<String,String> userMap =new HashMap<String,String>();
        Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        try{
        conn = DbConnectionManager.getConnection();
        pts = conn.prepareStatement(QUERY_ALL_IOSTOKEN);
        rs = pts.executeQuery();
        while (rs.next())
        {
            String userName = rs.getString("userName");
            String iosToken = rs.getString("iosToken");
            userMap.put(iosToken, userName);
        }
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("获取所有token失败:"+e.getMessage());
        }finally{
        	DbConnectionManager.closeConnection(rs, pts, conn);
        }
        
        return userMap;
    }
    /**
     * 根据token查询用户
     * @Title: getUserNameByToken
     * @Description: TODO  
     * @param: @param token
     * @param: @return   
     * @return: Map<String,String>
     * @author: sunshine  
     * @throws
     */
    public String getUserNameByToken(String token)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        String userName=null;
        try{
    	 conn = DbConnectionManager.getConnection();
    	 PreparedStatement pst = conn.prepareStatement(QUERY_USERNAME_BYIOSTOKEN);
    	 pst.setString(1, token);
    	 rs = pst.executeQuery();
    	 rs.next();
		 userName = rs.getString("userName");
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbConnectionManager.closeConnection(rs, pts, conn);
        }
		return userName;
    }
    /**
     * 根据用户查询token
     * @Title: getTokenByUserName
     * @Description: TODO  
     * @param: @param userName
     * @param: @return     
     * @return: String
     * @author: sunshine  
     * @throws
     */
    public String getTokenByUserName(String userName)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        String iosToken=null;
        try{
    	 conn = DbConnectionManager.getConnection();
    	 PreparedStatement pst = conn.prepareStatement(QUERY_IOSTOKEN_BYUSERNAME);
    	 pst.setString(1, userName);
    	 rs = pst.executeQuery();
    	 rs.next();
    	 iosToken  = rs.getString("iosToken");
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("更具key获取token失败:"+e.getMessage());
        }finally{
        	DbConnectionManager.closeConnection(rs, pts, conn);
        }
    	return iosToken;

    }
    
    public boolean delUserByToken(String token)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        int rs=0;
        try{
    	 conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYIOSTOKEN);
    	pst.setString(1, token);
    	rs = pst.executeUpdate();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbConnectionManager.closeConnection(pts, conn);
        }
    	if(rs>0){
    		return true;
    	}else{
    		log.error("删除用户设备失败");
    	}
    	return false;
    }
    
    public boolean delTokenByUser(String userName)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        int rs=0;
        try{
    	conn = DbConnectionManager.getConnection();
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYUSERNAME);
    	pst.setString(1, userName);
    	rs = pst.executeUpdate();
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("删除iostoken失败:"+e.getMessage());
        }finally{
        	DbConnectionManager.closeConnection(pts, conn);
        }
    	if(rs>0){
    		return true;
    	}
    	return false;
    }
    
    public boolean saveIosToken(String userName,String token)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        int rs =0;
        try{
    	 conn = DbConnectionManager.getConnection();
    	 PreparedStatement pst = conn.prepareStatement(INSERT_OFDEVICETOKEN);
    	 pst.setString(1, userName);
    	 pst.setString(2, token);
    	 rs = pst.executeUpdate();
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("保存iostoken失败:"+e.getMessage());
        }finally{
        	DbConnectionManager.closeConnection(pts, conn);
        }
    	if(rs>0){
    		return true;
    	}
    	return false;
    }
}