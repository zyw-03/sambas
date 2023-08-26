package com.zyw.dao.user;

import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    // 根据userCode来查找用户
    public User getLoginUser(Connection connection,  String userCode, String password) throws SQLException;

    //修改密码
    public int savePassword(Connection connection, String oldPassword, String newPassword) throws SQLException;
}
