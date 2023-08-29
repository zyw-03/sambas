package com.zyw.dao.provider;

import com.zyw.pojo.Provider;
import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {

    //返回provider列表
    public List<Provider> getProList(Connection connection, String providerCode, String providerName) throws SQLException;
    //根据供应商编号或供应商名字查找对应的供应商的人数
    public int getProCount(Connection connection, String providerCode, String providerName) throws SQLException;

    //添加一个供应商
    public int addProvider(Connection connection, Provider provider) throws SQLException;

    //根据proid删除用户
    public int delProvider(Connection connection, int proid) throws SQLException;

    //根据id找到相应的供货商
    public Provider getProviderById(Connection connection, int id)throws Exception;


    //修改供应商
    public int modify(Connection connection, Provider provider)throws Exception;

}
