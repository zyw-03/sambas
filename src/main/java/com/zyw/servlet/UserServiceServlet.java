package com.zyw.servlet;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyw.pojo.User;
import com.zyw.service.UserService;
import com.zyw.service.UserServiceImp;
import com.zyw.utils.Contains;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class UserServiceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")){
            this.savePassword(req, resp);
        }
        else if(method.equals("pwdmodify")){
            this.pwdModify(req, resp);
        }
    }

    public void savePassword(HttpServletRequest rep, HttpServletResponse resp) throws ServletException, IOException {
        String oldPassword = rep.getParameter("oldpassword");
        String newPassword = rep.getParameter("rnewpassword");

        UserService userServiceImp = new UserServiceImp();

        int res = userServiceImp.savePassword(oldPassword, newPassword);

        if(res > 0) {
            rep.setAttribute(Contains.SYS_MESSAGE, "修改密码成功");
        }
        else {
            rep.setAttribute(Contains.SYS_MESSAGE, "修改密码失败");
        }
        rep.getRequestDispatcher("pwdmodify.jsp").forward(rep, resp);
    }

    public void pwdModify(HttpServletRequest rep, HttpServletResponse resp) throws IOException {
        User loginUser = (User) rep.getSession().getAttribute(Contains.USER_SESSION);
        HashMap<String, String> res = new HashMap<String, String>();
        String oldPassword = rep.getParameter("oldpassword");

        if(loginUser == null) {
            res.put("result", "sessionerror");
        }
        else if(StringUtils.isNullOrEmpty(oldPassword)){
            res.put("result", "error");
        }
        else if(!loginUser.getUserPassword().equals(oldPassword)) {
            res.put("result", "false");
        }
        else{
            res.put("result", "true");
        }

        resp.setContentType("application/json");
        String jsonString = JSONArray.toJSONString(res);
        PrintWriter out = resp.getWriter();

        out.write(jsonString);
        out.flush();
        out.close();

    }
}
