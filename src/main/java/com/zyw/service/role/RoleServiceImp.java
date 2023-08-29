package com.zyw.service.role;

import com.zyw.dao.BaseDao;
import com.zyw.dao.role.RoleDao;
import com.zyw.dao.role.RoleDaoImp;
import com.zyw.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public class RoleServiceImp implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImp() {
        roleDao = new RoleDaoImp();
    }

    public List<Role> getRoleList() {
        List<Role> roleList = null;
        try {
            roleList = roleDao.getRoleList(BaseDao.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roleList;
    }
}
