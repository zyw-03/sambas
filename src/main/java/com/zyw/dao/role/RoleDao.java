package com.zyw.dao.role;

import com.zyw.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    //返回职位的列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
