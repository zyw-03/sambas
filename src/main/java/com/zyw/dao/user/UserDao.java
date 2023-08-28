package com.zyw.dao.user;

import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    // 根据userCode来查找用户
    public User getLoginUser(Connection connection,  String userCode, String password) throws SQLException;

    //修改密码
    public int savePassword(Connection connection, String oldPassword, String newPassword) throws SQLException;

    //根据用户名字或职位查找对应的用户信息
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    //根据用户名字或职位查找对应的用户的人数
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    //添加一个用户
    public int addUser(Connection connection, User user) throws SQLException;

    //根据userCode判断是否已存在该用户
    public Boolean userCodeExist(Connection connection, String userCode) throws SQLException;

    //根据id来拿到用户的信息
    public User getUserByID(Connection connection, int id) throws SQLException;

    //根据id来更新用户信息
    public int updateUser(Connection connection, int id, User user) throws SQLException;

    //根据id删除用户
    public int delUser(Connection connection, int id) throws SQLException;

}
