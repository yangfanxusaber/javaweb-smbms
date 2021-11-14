package com.codeleopard.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.codeleopard.pojo.Role;
import com.codeleopard.pojo.User;
import com.codeleopard.service.role.RoleServiceImpl;
import com.codeleopard.service.user.UserService;
import com.codeleopard.service.user.UserServiceImpl;
import com.codeleopard.util.Constants;
import com.codeleopard.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

// 实现servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd") && method!=null){
            this.updatePwd(req, resp);
        }else if(method.equals("pwdmodify") && method!=null){
            this.pwdModify(req, resp);
        }else if(method.equals("query") && method!=null){
            this.query(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd") && method!=null){
            this.updatePwd(req, resp);
        }
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从Session里面拿ID
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);

        String newPassword = req.getParameter("newpassword");

        System.out.println("UserServlet: " + newPassword);

        boolean flag = false;
        System.out.println("flag: " + flag);
        System.out.println("first: " + attribute != null);
        System.out.println("second: " + !StringUtils.isNullOrEmpty(newPassword));

        if(attribute != null && !StringUtils.isNullOrEmpty(newPassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) attribute).getId(), newPassword);
            if(flag){
                req.setAttribute("message", "修改密码成功，请退出，使用新密码登录！");
                // 密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message", "修改密码失败");
            }
        }else{
            req.setAttribute("message", "新密码有问题");
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    // 验证旧密码 Session中有用户的密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从Session里面拿ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        // 万能的map：结果集
        HashMap<String, String> resultMap = new HashMap<>();
        //  Session失效了或者过期了
        if(o == null){
            resultMap.put("result", "SessionError");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){   // 输入的密码为空
            resultMap.put("result", "error");
        }else{
            String userPassword = ((User) o).getUserPassword();
            if(oldpassword.equals(userPassword)){
                resultMap.put("result", "true");
            }else{
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        // JSONArray 阿里巴巴的工具类，转换格式。
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    // 查询用户列表（重点 + 难点）
    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 查询用户列表

        // 从前端获取数据
        String queryname = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;
        
        // 获取用户列表
        UserServiceImpl userService = new UserServiceImpl();

        // 第一次走这个请求，一定是第一页，页面大小固定的。
        int pageSize = 5;   // 将这个写入配置文件中，方便后期修改
        int currPageNo = 1;

        if(queryname == null){
            queryname = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp); // 给查询赋值
            
        }
        if(pageIndex!=null){
            currPageNo = Integer.parseInt(pageIndex);
        }

        // 获取用户的总数（分页：上一页、下一页的情况）
        int totalCount = userService.getUserCount(queryname, queryUserRole);
        // 总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();

        // 控制首页和尾页
        if(totalCount < 1){ // 如果用户在第一页还想往前翻
            currPageNo =1;
        }else if(currPageNo > totalPageCount){
            currPageNo = totalPageCount;    // 如果用户在最后一页还想往后翻
        }

        // 获取用户列表展示
        List<User> userList = userService.getUserList(queryname, queryUserRole, currPageNo, pageSize);
        req.setAttribute("userList", userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryname);
        req.setAttribute("queryUserRole", queryUserRole);

        // 返回前端
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }
}
