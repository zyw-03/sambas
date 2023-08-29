package com.zyw.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyw.pojo.Provider;
import com.zyw.pojo.Role;
import com.zyw.pojo.User;
import com.zyw.service.provider.ProviderService;
import com.zyw.service.provider.ProviderServiceImp;
import com.zyw.service.role.RoleService;
import com.zyw.service.role.RoleServiceImp;
import com.zyw.service.user.UserService;
import com.zyw.service.user.UserServiceImp;
import com.zyw.utils.Constants;
import com.zyw.utils.PageSupport;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class providerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if(method.equals("query")){
            try {
                this.query(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("add")){
            try {
                this.addProvider(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("delprovider")){
            try {
                this.delPro(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("view")){
            try {
                this.proView(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("modify")){
            try {
                this.modify(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("modifysave")){
            try {
                this.modifySave(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void modifySave(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        String id = req.getParameter("id");

        Provider provider = new Provider();
        provider.setProName(proName);
        provider.setId(Integer.valueOf(id));
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean flag = false;
        ProviderService providerService = new ProviderServiceImp();
        flag = providerService.modify(provider);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = Integer.parseInt(req.getParameter("proid"));

        ProviderService imp = new ProviderServiceImp();
        Provider provider = imp.getProviderById(id);

        req.setAttribute("provider", provider);

        req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
    }

    private void proView(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int proid = Integer.parseInt(req.getParameter("proid"));

        ProviderService imp = new ProviderServiceImp();

        Provider provider = imp.getProviderById(proid);

        req.setAttribute("provider", provider);

        req.getRequestDispatcher("/jsp/providerview.jsp").forward(req, resp);
    }

    private void delPro(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int proid = 0;
        HashMap<String, String> hashMap = new HashMap<String, String>();

        proid = Integer.parseInt(req.getParameter("proid"));

        if(proid <= 0){
            hashMap.put("delResult", "notexist");
        }
        else{
            ProviderService imp = new ProviderServiceImp();
            int count = imp.delPro(proid);

            if(count == 0){
                hashMap.put("delResult", "true");
            }
            else if(count == -1){
                hashMap.put("delResult", "false");
            }
            else{
                hashMap.put("delResult", count + "");
            }
        }

        resp.setContentType("Application/json");
        PrintWriter out = resp.getWriter();

        out.write(JSONArray.toJSONString(hashMap));
        out.flush();
        out.close();
    }

    private void addProvider(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);

        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        boolean flag = false;
        ProviderService providerService = new ProviderServiceImp();
        flag = providerService.addProvider(provider);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("provideradd.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");

        ProviderService providerService = new ProviderServiceImp();
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }


        List<Provider> providerList = new ArrayList<Provider>();

        providerList = providerService.getProList(queryProCode,queryProName);
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("queryProCode", queryProCode);

        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }


}
