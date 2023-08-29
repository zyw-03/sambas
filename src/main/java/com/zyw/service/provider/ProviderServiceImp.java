package com.zyw.service.provider;

import com.zyw.dao.BaseDao;
import com.zyw.dao.provider.ProviderDao;
import com.zyw.dao.provider.ProviderDaoImp;
import com.zyw.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImp implements ProviderService{

    private ProviderDao providerDao = null;

    public ProviderServiceImp() {
        providerDao = new ProviderDaoImp();
    }

    public List<Provider> getProList(String providerCode, String providerName) throws SQLException {
        Connection connection = BaseDao.getConnection();

        List<Provider> proList = providerDao.getProList(connection, providerCode, providerName);

        BaseDao.closeAll(connection, null, null);

        return proList;
    }

    public int getProCount(String providerCode, String providerName) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int res = providerDao.getProCount(connection, providerCode, providerName);
        BaseDao.closeAll(connection, null, null);

        return res;
    }

    public Boolean addProvider(Provider provider) throws SQLException {
        Boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int rows = providerDao.addProvider(connection, provider);

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

    public int delPro(int proid) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int count = providerDao.delProvider(connection, proid);

        return count;
    }

    public Provider getProviderById(int id) throws Exception {
        Provider provider = null;
        Connection connection = null;
        connection = BaseDao.getConnection();
        provider = providerDao.getProviderById(connection, id);

        BaseDao.closeAll(connection, null, null);

        return provider;

    }

    public boolean modify(Provider provider) throws Exception {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;

        if(providerDao.modify(connection,provider) > 0) {
            flag = true;
        }
        BaseDao.closeAll(connection, null, null);

        return flag;
    }
}
