package com.zyw.dao.user;

import com.zyw.dao.BaseDao;
import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDaoImp implements UserDao {


    public User getLoginUser(Connection connection, String userCode, String password) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        User user = null;

        if(connection != null){
            String sql = "select * from smbms_user where userCode = ? and userPassword = ?";
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(userCode);
            params.add(password);
            rs = BaseDao.executeQuery(connection, ps, sql, params.toArray());
        }

        if(rs.next()){
            user = new User();
            user.setId(rs.getInt("id"));
            user.setUserCode(rs.getString("userCode"));
            user.setUserName(rs.getString("userName"));
            user.setUserPassword(rs.getString("userPassword"));
            user.setGender(rs.getInt("gender"));
            user.setBirthday(rs.getDate("birthday"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            user.setUserRole(rs.getInt("userRole"));
            user.setCreatedBy(rs.getInt("createdBy"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setModifyBy(rs.getInt("modifyBy"));
            user.setModifyDate(rs.getTimestamp("modifyDate"));
        }

        BaseDao.closeAll(null, ps, rs);

        return user;
    }

    public int savePassword(Connection connection, String oldPassword, String newPassword) throws SQLException {
        PreparedStatement preparedStatement = null;
        int res = 0;
        if(connection != null){
            String sql = "update smbms_user set userPassword = ? where userPassword = ?";
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(newPassword);
            params.add(oldPassword);
            res = BaseDao.executeUpdate(connection, preparedStatement, sql, params.toArray());
        }
        BaseDao.closeAll(null, preparedStatement, null);
        return res;

    }
}
