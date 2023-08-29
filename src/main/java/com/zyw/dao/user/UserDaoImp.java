package com.zyw.dao.user;

import com.mysql.jdbc.StringUtils;
import com.zyw.dao.BaseDao;
import com.zyw.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            rs = BaseDao.executeQuery(connection, pstm, sql.toString(), params);
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return userList;
    }

    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(*) count from smbms_user ");
            List<Object> list = new ArrayList<Object>();
            boolean flag = false;

            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append("where userName like ?");
                list.add("%" + userName + "%");
                flag = true;
            }
            if(userRole > 0){
                if(flag)    sql.append("and ");
                else sql.append("where ");

                sql.append("userRole = ?");
                list.add(userRole);
            }

            rs = BaseDao.executeQuery(connection, pstm, sql.toString(), list.toArray());
        }
        if(rs.next()) {
            count = rs.getInt("count");
        }
        BaseDao.closeAll(null, pstm, rs);
        return count;
    }

    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(null != connection){
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            updateRows = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return updateRows;
    }

    public Boolean userCodeExist(Connection connection, String userCode) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Object> params = null;

        if(connection != null){
            params = new ArrayList<Object>();
            String sql = "select * from smbms_user where usercode = ?";
            params.add(userCode);

            rs = BaseDao.executeQuery(connection, ps, sql, params.toArray());

            if(rs.next()){
                BaseDao.closeAll(null, ps, rs);
                return true;
            }
            BaseDao.closeAll(null, ps, rs);
        }
        return false;
    }

    public User getUserByID(Connection connection, int id) throws SQLException {
        User user = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};
            rs = BaseDao.executeQuery(connection, pstm, sql, params);

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
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return user;

    }

    public int updateUser(Connection connection, int id, User user) throws SQLException {
        int rows = 0;
        PreparedStatement ps = null;

        if(connection != null){
            String sql = "update smbms_user set userName = ?, gender = ?, birthday = ?, phone = ?, " +
                         "address = ?, userRole = ?, modifyBy = ?, modifyDate = ? where id = ?";
            Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(), user.getPhone(),
                               user.getAddress(), user.getUserRole(), user.getModifyBy(), user.getModifyDate(), id};
            rows = BaseDao.executeUpdate(connection, ps, sql, params);
        }
        BaseDao.closeAll(null, ps, null);

        return rows;
    }

    public int delUser(Connection connection, int id) throws SQLException {
        int rows = 0;
        PreparedStatement ps = null;
        if(connection != null){
            String sql = "delete from smbms_user where id = ?" ;
            Object[] params = {id};
            rows = BaseDao.executeUpdate(connection, ps, sql, params);
        }
        BaseDao.closeAll(null, ps, null);

        return rows;
    }


}
