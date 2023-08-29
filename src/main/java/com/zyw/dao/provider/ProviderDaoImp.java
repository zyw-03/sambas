package com.zyw.dao.provider;

import com.mysql.jdbc.StringUtils;
import com.zyw.dao.BaseDao;
import com.zyw.dao.bill.BillDao;
import com.zyw.dao.bill.BillDaoImp;
import com.zyw.pojo.Provider;
import com.zyw.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImp implements ProviderDao{
    public List<Provider> getProList(Connection connection, String providerCode, String providerName) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Provider> providerList = new ArrayList<Provider>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select * from smbms_provider ");
            List<Object> list = new ArrayList<Object>();
            boolean flag = false;

            if(!StringUtils.isNullOrEmpty(providerName)){
                sql.append("where proName like ? ");
                list.add("%" + providerName + "%");
                flag = true;
            }
            if(!StringUtils.isNullOrEmpty(providerCode)){
                if(flag)    sql.append("and ");
                else sql.append("where ");

                sql.append("proCode like ? ");
                list.add("%" + providerCode + "%");
            }

            Object[] params = list.toArray();
            rs = BaseDao.executeQuery(connection, pstm, sql.toString(), params);
            while(rs.next()){
                Provider provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreationDate(rs.getTimestamp("creationDate"));
                providerList.add(provider);
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return providerList;
    }

    public int getProCount(Connection connection, String providerCode, String providerName) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) count from smbms_provider ");
            List<Object> list = new ArrayList<Object>();
            boolean flag = false;

            if (!StringUtils.isNullOrEmpty(providerName)) {
                sql.append("where proName like ? ");
                list.add("%" + providerName + "%");
                flag = true;
            }
            if (!StringUtils.isNullOrEmpty(providerCode)) {
                if (flag) sql.append("and ");
                else sql.append("where ");

                sql.append("proCode like ?");
                list.add("%" + providerCode + "%");
            }

            rs = BaseDao.executeQuery(connection, pstm, sql.toString(), list.toArray());
            if (rs.next()) {
                count = rs.getInt("count");
            }
        }
        BaseDao.closeAll(null, pstm, rs);
        return count;
    }

    public int addProvider(Connection connection, Provider provider) throws SQLException {
        PreparedStatement pstm = null;
        int flag = 0;
        if (connection != null) {
            String sql = "insert into smbms_provider (proCode,proName,proDesc," +
                    "proContact,proPhone,proAddress,proFax,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate()};
            flag = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return flag;
    }

    public int delProvider(Connection connection, int proid) throws SQLException {
        int rows = -1;
        PreparedStatement ps = null;
        if(connection != null){
            BillDao imp = new BillDaoImp();
            int count = imp.getBillCountByProId(connection, proid);
            if(count == 0){
                String sql = "delete from smbms_provider where id = ?" ;
                Object[] params = {proid};
                BaseDao.executeUpdate(connection, ps, sql, params);
                rows = 0;
            }
            else{
                rows = count;
            }

        }
        BaseDao.closeAll(null, ps, null);

        return rows;
    }

    public Provider getProviderById(Connection connection, int id) throws Exception {
        Provider provider = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (connection != null) {
            String sql = "select * from smbms_provider where id = ?";
            Object[] params = {id};
            rs = BaseDao.executeQuery(connection, pstm, sql, params);
            if (rs.next()) {
                provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreatedBy(rs.getInt("createdBy"));
                provider.setCreationDate(rs.getTimestamp("creationDate"));
                provider.setModifyBy(rs.getInt("modifyBy"));
                provider.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return provider;
    }

    public int modify(Connection connection, Provider provider) throws Exception {
        int flag = 0;
        PreparedStatement pstm = null;
        if (connection != null) {
            String sql = "update smbms_provider set proName=?,proDesc=?,proContact=?," +
                    "proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {provider.getProName(), provider.getProDesc(), provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getModifyBy(), provider.getModifyDate(), provider.getId()};
            flag = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return flag;
    }
}
