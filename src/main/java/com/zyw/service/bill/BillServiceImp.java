package com.zyw.service.bill;

import com.zyw.dao.BaseDao;
import com.zyw.dao.bill.BillDao;
import com.zyw.dao.bill.BillDaoImp;
import com.zyw.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImp implements BillService{
    private BillDao billDao;

    public BillServiceImp() {
        billDao = new BillDaoImp();
    }

    public List<Bill> getBillList(String queryProductName, int queryProductId, int queryIsPayment) throws Exception {
        Connection connection = null;
        List<Bill> billList = null;

        connection = BaseDao.getConnection();
        billList = billDao.getBillList(connection, queryProductName, queryProductId, queryIsPayment);

        BaseDao.closeAll(connection, null, null);

        return billList;
    }

    public boolean addBill(Bill bill) throws SQLException {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if(billDao.addBill(connection,bill) > 0) {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            BaseDao.closeAll(connection, null, null);
        }
        return flag;
    }

    public Bill getBillById(int id) throws SQLException {
        Bill bill = null;
        Connection connection = BaseDao.getConnection();
        bill = billDao.getBillById(connection, id);

        BaseDao.closeAll(connection, null, null);

        return bill;
    }

    public boolean updateBill(Bill bill) throws SQLException {
        Connection connection = null;
        boolean flag = false;

        connection = BaseDao.getConnection();
        if(billDao.updateBill(connection, bill) > 0) flag = true;

        BaseDao.closeAll(connection, null, null);

        return flag;
    }

    public boolean delBill(int id) throws SQLException {
        Connection connection = null;
        boolean flag = false;

        connection = BaseDao.getConnection();
        if(billDao.delBill(connection, id) > 0) {
            flag = true;
        }

        BaseDao.closeAll(connection, null, null);

        return flag;

    }

}
