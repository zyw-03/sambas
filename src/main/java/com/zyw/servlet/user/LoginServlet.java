package com.zyw.servlet.user;

import com.zyw.pojo.User;
import com.zyw.service.user.UserService;
import com.zyw.service.user.UserServiceImp;
import com.zyw.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userCode = req.getParameter("userCode");
        String userPassword = (String) req.getParameter("userPassword");

        UserService userDaoImp = new UserServiceImp();
        User loginUser = null;
        try {
            loginUser = userDaoImp.login(userCode, userPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(loginUser != null){
            HttpSession session = req.getSession();
            session.setAttribute(Constants.USER_SESSION, loginUser);
            System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "  " + loginUser.getUserName() + "登录");
            resp.sendRedirect("/jsp/frame.jsp");
        }else{
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }


    }
}