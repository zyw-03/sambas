package com.zyw.filter;

import com.zyw.pojo.User;
import com.zyw.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GrantFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
        User user = (User) servletRequest1.getSession().getAttribute(Constants.USER_SESSION);

        if(user == null)    servletRequest1.getRequestDispatcher("/syserror.jsp").forward(servletRequest, servletResponse);

        filterChain.doFilter(servletRequest, servletResponse);

    }

    public void destroy() {

    }
}
