package com.zyw.service;

import com.zyw.dao.BaseDao;
import com.zyw.dao.user.UserDao;
import com.zyw.dao.user.UserDaoImp;
import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImp implements UserService{

    private UserDao userDao;

    public UserServiceImp() {
        userDao = new UserDaoImp();
    }

    public User login(String userCode, String password){
        Connection connection = null;


        User loginUser = null;
        try {
            connection = BaseDao.getConnection();
            loginUser = userDao.getLoginUser(connection, userCode, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeAll(connection, null, null);
        }


        return loginUser;
    }

}
