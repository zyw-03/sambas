package com.zyw.dao.bill;

import com.mysql.jdbc.StringUtils;
import com.zyw.dao.BaseDao;
import com.zyw.pojo.Bill;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImp implements BillDao{
    public List<Bill> getBillList(Connection connection, String queryProductName, int queryProductId, int queryIsPayment) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Bill> billList = new ArrayList<Bill>();

        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select b.*,p.proName as providerName from smbms_bill b, smbms_provider p where b.providerId = p.id ");
            List<Object> params = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(queryProductName)) {
                sql.append("and productName like ? ");
                params.add("%" + queryProductName + "%");
            }
            if (queryProductId > 0) {
                sql.append("and providerId = ? ");
                params.add(queryProductId);
            }
            if (queryIsPayment > 0) {
                sql.append("and isPayment = ?");
                params.add(queryIsPayment);
            }


            rs = BaseDao.executeQuery(connection, pstm, sql.toString(), params.toArray());

            while (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("providerName"));
                bill.setCreationDate(rs.getTimestamp("creationDate"));
                bill.setCreatedBy(rs.getInt("createdBy"));
                billList.add(bill);
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return billList;
    }

    public int addBill(Connection connection, Bill bill) throws SQLException {
        PreparedStatement pstm = null;
        int flag = 0;
        if (connection != null) {
            String sql = "insert into smbms_bill (billCode,productName,productDesc," +
                    "productUnit,productCount,totalPrice,isPayment,providerId,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getProviderId(), bill.getCreatedBy(), bill.getCreationDate()};
            flag = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return flag;
    }

    public Bill getBillById(Connection connection, int id) throws SQLException {
        Bill bill = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (null != connection) {
            String sql = "select b.*,p.proName as providerName from smbms_bill b, smbms_provider p " +
                    "where b.providerId = p.id and b.id=?";
            Object[] params = {id};
            rs = BaseDao.executeQuery(connection, pstm, sql, params);
            if (rs.next()) {
                bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("providerName"));
                bill.setModifyBy(rs.getInt("modifyBy"));
                bill.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeAll(null, pstm, rs);
        }
        return bill;
    }

    public int updateBill(Connection connection, Bill bill) throws SQLException {
        int flag = 0;
        PreparedStatement pstm = null;
        if (null != connection) {
            String sql = "update smbms_bill set productName=?," +
                    "productDesc=?,productUnit=?,productCount=?,totalPrice=?," +
                    "isPayment=?,providerId=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getProviderId(), bill.getModifyBy(), bill.getModifyDate(), bill.getId()};
            flag = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return flag;
    }

    public int delBill(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        int flag = 0;
        if (null != connection) {
            String sql = "delete from smbms_bill where id=?";
            Object[] params = {id};
            flag = BaseDao.executeUpdate(connection, pstm, sql, params);
            BaseDao.closeAll(null, pstm, null);
        }
        return flag;
    }

    public int getBillCountByProId(Connection connection, int proId) throws SQLException {
        int count = 0;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (null != connection) {
            String sql = "select count(1)  billCount from smbms_bill where" +
                    "	providerId = ?";
            Object[] params = {proId};
            rs = BaseDao.executeQuery(connection, pstm, sql, params);
            if (rs.next()) {
                count = rs.getInt("billCount");
            }
            BaseDao.closeAll(null, pstm, rs);
        }

        return count;
    }
}
