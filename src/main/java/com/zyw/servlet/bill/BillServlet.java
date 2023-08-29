package com.zyw.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyw.pojo.Bill;
import com.zyw.pojo.Provider;
import com.zyw.pojo.User;
import com.zyw.service.bill.BillService;
import com.zyw.service.bill.BillServiceImp;
import com.zyw.service.provider.ProviderService;
import com.zyw.service.provider.ProviderServiceImp;
import com.zyw.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("query")){
            try {
                this.query(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("add")){
            try {
                this.addBill(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("getproviderlist")){
            try {
                this.getProList(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("view")){
            try {
                this.billView(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("modify")){
            try {
                this.modify(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("modifysave")){
            try {
                this.modifySave(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("delbill")){
            try {
                this.delBill(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void delBill(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String billid = req.getParameter("billid");
        int id;
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(billid)){
            id = Integer.parseInt(billid);
            BillService billService = new BillServiceImp();
            boolean flag = billService.delBill(id);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void modifySave(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String id = req.getParameter("id");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        BillService billService = new BillServiceImp();
        flag = billService.updateBill(bill);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String id = req.getParameter("billid");
        if(!StringUtils.isNullOrEmpty(id)){
            BillService billService = new BillServiceImp();
            Bill bill = null;
            bill = billService.getBillById(Integer.parseInt(id));
            req.setAttribute("bill", bill);
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }

    private void billView(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String id = req.getParameter("billid");
        if(!StringUtils.isNullOrEmpty(id)){
            BillService billService = new BillServiceImp();
            Bill bill = null;
            bill = billService.getBillById(Integer.parseInt(id));
            req.setAttribute("bill", bill);
            req.getRequestDispatcher("billview.jsp").forward(req, resp);
        }
    }

    private void getProList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImp();
        providerList = providerService.getProList("","");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(providerList));

        out.flush();
        out.close();
    }

    private void addBill(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");

        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        BillService billService = new BillServiceImp();
        flag = billService.addBill(bill);

        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImp();
        providerList = providerService.getProList("","");
        req.setAttribute("providerList", providerList);

        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        int queryProId = 0;
        int queryIsPay = 0;


        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }

        if(!StringUtils.isNullOrEmpty(queryProviderId)){
            queryProId = Integer.parseInt(queryProviderId);
        }

        if(!StringUtils.isNullOrEmpty(queryIsPayment)){
            queryIsPay = Integer.parseInt(queryIsPayment);
        }

        List<Bill> billList = new ArrayList<Bill>();
        BillService imp = new BillServiceImp();

        billList = imp.getBillList(queryProductName, queryProId, queryIsPay);
        req.setAttribute("billList", billList);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }
}
