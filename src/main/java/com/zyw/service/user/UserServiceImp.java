package com.zyw.service.user;

import com.zyw.dao.BaseDao;
import com.zyw.dao.user.UserDao;
import com.zyw.dao.user.UserDaoImp;
import com.zyw.pojo.User;
import com.zyw.service.user.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImp implements UserService {

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

    public int savePassword(String oldPassword, String newPassword) {
        int res = 0;
        Connection connection = null;


        try {
            connection = BaseDao.getConnection();
            res = userDao.savePassword(connection, oldPassword, newPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeAll(connection, null, null);
        }

        return res;

    }

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) throws SQLException {
        Connection connection = BaseDao.getConnection();

        List<User> userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);

        BaseDao.closeAll(connection, null, null);

        return userList;
    }

    public int getUserCount(String userName, int userRole) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int res = userDao.getUserCount(connection, userName, userRole);
        BaseDao.closeAll(connection, null, null);

        return res;
    }

    public Boolean addUser(User user) {
        Boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int rows = userDao.addUser(connection, user);


            if(rows > 0){
                flag = true;
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();

            try{
                connection.rollback();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            BaseDao.closeAll(connection, null, null);
        }

        return flag;

    }

    public Boolean userCodeExist(String userCode) throws SQLException {
        Connection connection = BaseDao.getConnection();
        Boolean flag = false;

        flag = userDao.userCodeExist(connection, userCode);
        BaseDao.closeAll(connection, null, null);

        return flag;

    }

    public User getUserByID(int id) throws SQLException {
        Connection connection = BaseDao.getConnection();
        User user = null;

        user = userDao.getUserByID(connection, id);

        BaseDao.closeAll(connection, null, null);

        return user;

    }

    public Boolean updateUser(int id, User user) throws SQLException {
        Connection connection = BaseDao.getConnection();
        Boolean flag = false;

        if(userDao.updateUser(connection, id, user) > 0){
            flag = true;
        }

        BaseDao.closeAll(connection, null, null);

        return flag;
    }

    public Boolean delUser(int id) throws SQLException {
        Connection connection = BaseDao.getConnection();
        Boolean flag = false;
        if(userDao.delUser(connection, id) > 0){
            flag = true;
        }

        BaseDao.closeAll(connection, null, null);

        return flag;
    }
}

