package com.zyw.service;

import com.zyw.pojo.User;

import java.sql.SQLException;

public interface UserService {

    //根据用户名和密码实现登录
    public User login(String userCode, String password) throws SQLException;


}
