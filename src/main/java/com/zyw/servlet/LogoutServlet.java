package com.zyw.servlet;

import com.zyw.pojo.User;
import com.zyw.utils.Contains;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-------->LogoutServlet");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(Contains.USER_SESSION);
        System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "  " + user.getUserName() + "注销");
        session.removeAttribute(Contains.USER_SESSION);   //注销掉session中的USER_SESSION信息
        resp.sendRedirect("/login.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
