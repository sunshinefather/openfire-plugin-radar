package com.radar.extend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;

import com.radar.bean.User;
import com.radar.cache.ICacheService;
import com.radar.cache.TestCacheService;
import com.radar.common.SqlConstant;

public class UserDao
{
    public void loadAllUserToCache() throws SQLException
    {
        ICacheService cache = new TestCacheService();
        Connection conn = DbConnectionManager.getConnection();
        PreparedStatement pst = conn.prepareStatement(SqlConstant.LOAD_ALL_USER);
        ResultSet rs = pst.executeQuery();
        while (rs.next())
        {
            String userName = rs.getString("username");
            String encryptedPassword = rs.getString("encryptedPassword");
            User user = new User(userName,encryptedPassword);
            cache.set(userName, user);
        }
    }
}
