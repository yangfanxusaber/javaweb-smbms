package com.codeleopard.filter;


import com.codeleopard.pojo.User;
import com.codeleopard.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // 类型转换
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 过滤器，从Session中获取用户
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user == null){   // 已经被移除、注销、未登录
            System.out.println("空了！空了！空了！");
            // request.getContextPath()可以得到网站的根目录
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }else{
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
