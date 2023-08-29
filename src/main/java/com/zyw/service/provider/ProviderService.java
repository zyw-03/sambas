package com.zyw.service.provider;

import com.zyw.pojo.Provider;
import com.zyw.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    //根据返回providerCode和providerName返回provider列表
    public List<Provider> getProList(String providerCode, String providerName) throws SQLException;

    //根据供应商编号或供应商名字查找对应的供应商的人数
    public int getProCount(String providerCode, String providerName) throws SQLException;

    //添加一个供应商
    public Boolean addProvider(Provider provider) throws SQLException;
    //根据proid来删除用户
    public int delPro(int proid) throws SQLException;

    //通过id来拿到相应的供货商
    public Provider getProviderById(int id) throws Exception;

    //修改供应商的信息
    public boolean modify(Provider provider) throws Exception;
}
