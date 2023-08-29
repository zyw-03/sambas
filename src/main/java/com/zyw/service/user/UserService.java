package com.zyw.service.user;

import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    //根据用户名和密码实现登录
    public User login(String userCode, String password) throws SQLException;

    //修改密码
    public int savePassword(String oldPassword, String newPassword);

    //根据用户名字或位置查找对应的用户信息
    public List<User> getUserList(String queryUserName, int queryUserRole,  int currentPageNo, int pageSize) throws SQLException;

    //根据用户名字或职位查找对应的用户的人数
    public int getUserCount(String userName, int userRole) throws SQLException;

    //增加一个用户
    public Boolean addUser(User user) throws SQLException;


    //根据userCode判断是否已存在该用户
    public Boolean userCodeExist(String userCode) throws SQLException;

    //根据id查找用户
    public User getUserByID(int id) throws SQLException;

    //根据id来更新用户
    public Boolean updateUser(int id, User user) throws SQLException;

    //根据id来删除用户
    public Boolean delUser(int id) throws SQLException;


}
