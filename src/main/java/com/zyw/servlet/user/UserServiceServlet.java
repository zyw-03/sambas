package com.zyw.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyw.pojo.Role;
import com.zyw.pojo.User;
import com.zyw.service.role.RoleService;
import com.zyw.service.role.RoleServiceImp;
import com.zyw.service.user.UserService;
import com.zyw.service.user.UserServiceImp;
import com.zyw.utils.Constants;
import com.zyw.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserServiceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
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
        else if(method.equals("query")){
            try {
                this.userQuery(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("add")){
            try {
                this.userAdd(req, resp);
                System.out.println("addUser");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("getrolelist")){
            this.getRoleList(req, resp);
        }
        else if(method.equals("ucexist")){
            try {
                this.userCodeExist(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("modifyexe")){
            try {
                this.updateUser(req, resp);
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
        else if(method.equals("deluser")){
            try {
                this.delUser(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(method.equals("view")){
            try {
                this.userView(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }


    private void savePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oldPassword = req.getParameter("oldpassword");
        String newPassword = req.getParameter("rnewpassword");

        UserService userServiceImp = new UserServiceImp();

        int res = userServiceImp.savePassword(oldPassword, newPassword);

        if(res > 0) {
            req.setAttribute(Constants.SYS_MESSAGE, "修改密码成功");
        }
        else {
            req.setAttribute(Constants.SYS_MESSAGE, "修改密码失败");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loginUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        HashMap<String, String> res = new HashMap<String, String>();
        String oldPassword = req.getParameter("oldpassword");

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

    private void userQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        System.out.println("user   " + pageIndex);

        //获取用户列表
        UserService userService = new UserServiceImp();

        //第一次走页面一定是第一页,页面大小固定的
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);//给查询赋值! 默认为0
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }

        //获取用户总数量（分页：上一页 下一页的情况）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pages = new PageSupport();
        //设置当前页码
        pages.setCurrentPageNo(currentPageNo);
        //设置页总大小
        pages.setPageSize(pageSize);
        //设置页总数量
        pages.setTotalCount(totalCount);

        //控制首页和尾页
        int totalPageCount = pages.getTotalPageCount();

        if (currentPageNo < 1) {  //显示第一页的东西
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {//当前页面大于最后一页，让它为最后一页就行
            currentPageNo = totalPageCount;
        }



        List<User> userList = null;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImp();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

    private void userAdd(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        User user = new User();
        user.setUserCode(req.getParameter("userCode"));
        user.setUserName(req.getParameter("userName"));
        user.setUserPassword(req.getParameter("ruserPassword"));
        user.setGender(Integer.parseInt(req.getParameter("gender")));
        try {
            user.setBirthday(new SimpleDateFormat("yy-MM-dd").parse(req.getParameter("birthday")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setUserRole(Integer.parseInt(req.getParameter("userRole")));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService imp = new UserServiceImp();
        Boolean flag = imp.addUser(user);
        if(flag){
            resp.sendRedirect("/jsp/user.do?method=query");
        }
        else{
            req.getRequestDispatcher("useradd.jsp");
        }

    }
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RoleService imp = new RoleServiceImp();

        List<Role> roleList = imp.getRoleList();

        resp.setContentType("application/json");
        String jsonString = JSONArray.toJSONString(roleList);
        PrintWriter out = resp.getWriter();

        out.write(jsonString);
        out.flush();
        out.close();

    }

    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String userCode = req.getParameter("userCode");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        UserService imp = new UserServiceImp();

        if(imp.userCodeExist(userCode)){
            hashMap.put("userCode", "exist");
        }
        else{
            hashMap.put("userCode", "useful");
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(JSONArray.toJSONString(hashMap));

        out.flush();
        out.close();

    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("uid"));

        UserService imp = new UserServiceImp();
        User user = imp.getUserByID(id);

        req.setAttribute("user", user);

        req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("uid"));
        User user = new User();
        user.setUserName(req.getParameter("userName"));
        user.setGender(Integer.parseInt(req.getParameter("gender")));
        try {
            user.setBirthday(new SimpleDateFormat("yy-MM-dd").parse(req.getParameter("birthday")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.setPhone(req.getParameter("phone"));
        user.setAddress(req.getParameter("address"));
        user.setUserRole(Integer.parseInt(req.getParameter("userRole")));
        user.setModifyBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService imp = new UserServiceImp();
        Boolean flag = false;

        flag = imp.updateUser(id, user);

        if(flag){
            resp.sendRedirect("/jsp/user.do?method=query");
        }
        else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

    }

    private void delUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int id = 0;
        HashMap<String, String> hashMap = new HashMap<String, String>();

        id = Integer.parseInt(req.getParameter("uid"));

        if(id <= 0){
            hashMap.put("delResult", "notexist");
        }
        else{
            UserService imp = new UserServiceImp();

            if(imp.delUser(id)){
                hashMap.put("delResult", "true");
            }
            else{
                hashMap.put("delResult", "false");
            }
        }

        resp.setContentType("Application/json");
        PrintWriter out = resp.getWriter();

        out.write(JSONArray.toJSONString(hashMap));
        out.flush();
        out.close();

    }

    private void userView(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int uid = Integer.parseInt(req.getParameter("uid"));

        UserService imp = new UserServiceImp();

        User user = imp.getUserByID(uid);
        System.out.println(user);

        req.setAttribute("user", user);

        req.getRequestDispatcher("/jsp/userview.jsp").forward(req, resp);
    }


}
