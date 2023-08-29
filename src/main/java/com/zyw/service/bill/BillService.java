package com.zyw.service.bill;

import com.zyw.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillService {
    //根据供应商、商品名称、是否付款来查询账单
    public List<Bill> getBillList(String queryProductName, int queryProductId, int queryIsPayment) throws Exception;

    //添加一个订单
    public boolean addBill(Bill bill) throws SQLException;

    //通过id来查找订单
    public Bill getBillById(int id) throws SQLException;

    //更新订单信息
    public boolean updateBill(Bill bill) throws SQLException;

    //根据id来删除订单
    public boolean delBill(int id) throws SQLException;

}
