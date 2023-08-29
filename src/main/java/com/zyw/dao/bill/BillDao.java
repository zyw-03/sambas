package com.zyw.dao.bill;

import com.zyw.pojo.Bill;
import com.zyw.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {

    //根据供应商、商品名称、是否付款来查询账单
    public List<Bill> getBillList(Connection connection, String queryProductName, int queryProductId, int queryIsPayment) throws Exception;

    //添加一个订单
    public int addBill(Connection connection, Bill bill) throws SQLException;

    //通过id来查找订单
    public Bill getBillById(Connection connection, int id) throws SQLException;

    //更新订单信息
    public int updateBill(Connection connection, Bill bill) throws SQLException;

    //根据id来删除订单
    public int delBill(Connection connection, int id) throws SQLException;

    //根据供货商的id来获取其订单数
    public int getBillCountByProId(Connection connection, int proId) throws SQLException;


}
