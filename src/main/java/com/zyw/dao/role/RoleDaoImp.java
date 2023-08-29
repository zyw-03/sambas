package com.zyw.dao.role;

import com.mysql.jdbc.StringUtils;
import com.zyw.dao.BaseDao;
import com.zyw.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImp implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        List<Role> roleList = null;

        if (connection != null){
            roleList = new ArrayList<Role>();

            String sql = "select * from smbms_role";
            Object[] params = {};

            resultSet = BaseDao.executeQuery(connection, ps, sql, params);
            while (resultSet.next()){
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setRoleCode(resultSet.getString("roleCode"));
                role.setRoleName(resultSet.getString("roleName"));
                roleList.add(role);
            }
        }
        BaseDao.closeAll(null, ps, resultSet);

        return roleList;
    }

}