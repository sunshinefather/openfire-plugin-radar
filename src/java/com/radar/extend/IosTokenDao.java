package com.radar.extend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IosTokenDao {
    private static final Logger log = LoggerFactory.getLogger(IosTokenDao.class);
    
    /**
     * 插入设备token
     */
    public static final String INSERT_OFDEVICETOKEN ="INSERT INTO ofdevicetoken (userName, iosToken) VALUES (?,?)";
    /**
     * 插入IOS用户信息
     */
    public static final String INSERT_USERINFO ="INSERT INTO ofdevicetoken (userName,iosToken,appName,userType,version) VALUES (?,?,?,?,?)";
    /**
     * 更新ios用户信息
     */
    public static final String UPDATE_USERINFO ="update ofdevicetoken set iosToken=?,appName=?,userType=?,version=? where userName=?";
    
    /**
     * 查询所有设备token
     */
    public static final String QUERY_ALL_IOSTOKEN ="SELECT userName,iosToken FROM ofdevicetoken";
    
    /**
     * 分页查询设备token
     */
    public static final String QUERY_IOSTOKEN_PAGE ="SELECT userName,iosToken FROM ofdevicetoken ORDER BY userName limit ?,?";
    
    /**
     * 统计设备token数据
     */
    public static final String QUERY_IOSTOKEN_TOTAL ="SELECT count(*) as ct from ofdevicetoken";
    
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
    @Deprecated
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
        	log.error("获取所有token失败",e);
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
    public String getUserNameByToken(String token,Connection... connection)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        String userName=null;
        try{
         if(connection!=null && connection.length>0){
        	 conn = connection[0];
         }else{
        	 conn = DbConnectionManager.getConnection(); 
         }
    	 PreparedStatement pst = conn.prepareStatement(QUERY_USERNAME_BYIOSTOKEN);
    	 pst.setString(1, token);
    	 rs = pst.executeQuery();
    	 boolean hasNext =  rs.next();
    	 if(hasNext){
		  userName = rs.getString("userName");
		 }
        }catch(Exception e){
        	e.printStackTrace();
        	log.warn("根据token获取用户名为空token为:"+token,e);
        }finally{
        	if(connection!=null && connection.length>0){
        		DbConnectionManager.closeConnection(rs, pts,null);
            }else{
            	DbConnectionManager.closeConnection(rs, pts, conn);
            }
        }
		return userName;
    }
    
    /**
     * 统计设备token
     * @Title: getTotal
     * @Description: TODO  
     * @param: @return      
     * @return: int
     * @author: sunshine  
     * @throws
     */
    public int getTotal()
    {
    	Connection conn = null;
    	PreparedStatement pts = null;
    	ResultSet rs =null;
    	int total=0;
    	try{
    		conn = DbConnectionManager.getConnection();
            pts = conn.prepareStatement(QUERY_IOSTOKEN_TOTAL);
            rs = pts.executeQuery();
    		boolean hasNext =  rs.next();
    		if(hasNext){
    			total = rs.getInt("ct");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		log.warn("@sunshine:统计设备数错误",e);
    	}finally{
    		DbConnectionManager.closeConnection(rs, pts, conn);
    	}
    	return total;
    }
    /**
     * 分页获取 userName+","+token
     * @Title: getTokenByPage
     * @Description: TODO  
     * @param: @param page
     * @param: @return      
     * @return: PaginationAble
     * @author: sunshine  
     * @throws
     */
    public PaginationAble getTokenByPage(PaginationAble page)
    {
        List<String> list =new ArrayList<String>();
        Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        try{
        conn = DbConnectionManager.getConnection();
        pts = conn.prepareStatement(QUERY_IOSTOKEN_PAGE);
        pts.setInt(1,page.getCurrentResult());
        pts.setInt(2,page.getPageSize());
        
        rs = pts.executeQuery();
        while (rs.next())
        {
            String userName = rs.getString("userName");
            String iosToken = rs.getString("iosToken");
            // String appName = rs.getString("appName");
            //String userType = rs.getString("userType");
            //String version = rs.getString("version");
            //list.add(userName+","+iosToken+","+appName+","+userType+","+version);
            list.add(userName+","+iosToken);
        }
        page.setResults(list);
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("@sunshine:分页获取token失败",e);
        }finally{
        	DbConnectionManager.closeConnection(rs, pts, conn);
        }
        
        return page;
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
    public String getTokenByUserName(String userName,Connection...connection)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        ResultSet rs =null;
        String iosToken=null;
        try{
        	 if(connection!=null && connection.length>0){
            	 conn = connection[0];
             }else{
            	 conn = DbConnectionManager.getConnection(); 
             }
    	 PreparedStatement pst = conn.prepareStatement(QUERY_IOSTOKEN_BYUSERNAME);
    	 pst.setString(1, userName);
    	 rs = pst.executeQuery();
    	 boolean hasNext =  rs.next();
    	 if(hasNext){
        	 //iosToken  = rs.getString("iosToken")+","+rs.getString("version");
        	 iosToken  = rs.getString("iosToken");
    	 }
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("根据key获取token失败:"+userName,e);
        }finally{
        	if(connection!=null && connection.length>0){
        		DbConnectionManager.closeConnection(rs, pts,null);
            }else{
            	DbConnectionManager.closeConnection(rs, pts, conn);
            }
        }
    	return iosToken;

    }
    
    public boolean delUserByToken(String token,Connection...connection)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        int rs=0;
        try{
        	if(connection!=null && connection.length>0){
           	 conn = connection[0];
            }else{
           	 conn = DbConnectionManager.getConnection(); 
            }
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYIOSTOKEN);
    	pst.setString(1, token);
    	rs = pst.executeUpdate();
        }catch(Exception e){
        	e.printStackTrace();
           log.error("删除用户设备失败:"+token,e);
        }finally{
        	if(connection!=null && connection.length>0){
        		DbConnectionManager.closeConnection(null, pts,null);
            }else{
            	DbConnectionManager.closeConnection(null, pts, conn);
            }
        }
    	if(rs>0){
    		return true;
    	}
    	return false;
    }
    
    public boolean delTokenByUser(String userName,Connection...connection)
    {
    	Connection conn = null;
        PreparedStatement pts = null;
        int rs=0;
        try{
        	if(connection!=null && connection.length>0){
           	 conn = connection[0];
            }else{
           	 conn = DbConnectionManager.getConnection(); 
            }
    	PreparedStatement pst = conn.prepareStatement(DELETE_USERNAME_BYUSERNAME);
    	pst.setString(1, userName);
    	rs = pst.executeUpdate();
        }catch(Exception e){
        	e.printStackTrace();
        	log.error("删除iostoken失败:"+userName,e);
        }finally{
        	if(connection!=null && connection.length>0){
        		DbConnectionManager.closeConnection(null, pts,null);
            }else{
            	DbConnectionManager.closeConnection(null, pts, conn);
            }
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
        	log.error("保存iostoken失败",e);
        }finally{
        	DbConnectionManager.closeConnection(pts, conn);
        }
    	if(rs>0){
    		return true;
    	}
    	return false;
    }
    /**
     * 目前为了兼容版本,此处的保存用户信息非保存,删除新版本在本系统中存储的token，由极光服务器维护，保留旧版本,旧版本依旧使用本系统推送
     * @Title: saveUserInfo
     * @Description: TODO  
     * @param: @param userInfo
     * @param: @return      
     * @return: boolean
     * @author: sunshine  
     * @throws
     */
    public boolean saveUserInfo(UserInfo userInfo)
    {
    	Connection conn = null;
    	PreparedStatement pst = null;
    	int rs =0;
    	try{
    		conn = DbConnectionManager.getConnection();
    		//boolean isautocommit=conn.getAutoCommit();
    		//if(isautocommit){
    		//	conn.setAutoCommit(false);
    		//}
    		/**如果存在相同的token则删除*/
    		if(StringUtils.isNotBlank(userInfo.getIosToken())){
        		String userName =getUserNameByToken(userInfo.getIosToken(),conn);
        		if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(userInfo.getUserName()) && !userInfo.getUserName().equals(userName)){
        			delUserByToken(userInfo.getIosToken(), conn);
        		}
    		}
    		///**如果用户名和token不一致则删除*/
    		/**删除新版用户的token记录*/
    		if(StringUtils.isNotBlank(userInfo.getUserName())){
    			String token =getTokenByUserName(userInfo.getUserName(),conn);
    			if(StringUtils.isNotEmpty(token)){
    				//String[] ts =token.split("[,]");
    				//token=ts[0];
    				//String version =ts[1];
        			//if(StringUtils.isNotEmpty(userInfo.getIosToken()) && !token.equals(userInfo.getIosToken()) && !version.equals(userInfo.getVersion())){
        				delTokenByUser(userInfo.getUserName(), conn);
        			//}
    			}
    		}
    		/*
    		String token=getTokenByUserName(userInfo.getUserName(),conn);
    		if(StringUtils.isNotEmpty(token)){
    			//String[] ts =token.split("[,]");
				//token=ts[0];
				//String version =ts[1];
    			//if(!token.equals(userInfo.getIosToken()) && !version.equals(userInfo.getVersion())){
    				if(!token.equals(userInfo.getIosToken()) && !version.equals(userInfo.getVersion())){
        			//更新
        			pst = conn.prepareStatement(UPDATE_USERINFO);
            		pst.setString(1, userInfo.getIosToken());
            		pst.setString(2, userInfo.getAppName());
            		pst.setString(3, userInfo.getUserType());
            		pst.setString(4, userInfo.getVersion());
            		pst.setString(5, userInfo.getUserName());
            		rs = pst.executeUpdate();	
    			}
    		}else{
    			//插入
        		pst = conn.prepareStatement(INSERT_USERINFO);
        		pst.setString(1, userInfo.getUserName());
        		pst.setString(2, userInfo.getIosToken());
        		pst.setString(3, userInfo.getAppName());
        		pst.setString(4, userInfo.getUserType());
        		pst.setString(5, userInfo.getVersion());
        		rs = pst.executeUpdate();	
    		}
    		conn.commit();
    		if(isautocommit){
    			conn.setAutoCommit(true);
    		}
    		*/
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("保存iostoken失败",e);
    	}finally{
    		DbConnectionManager.closeConnection(pst, conn);
    	}
    	if(rs>0){
    		return true;
    	}
    	return false;
    }
}