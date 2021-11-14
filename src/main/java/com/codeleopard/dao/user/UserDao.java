package com.codeleopard.dao.user;

import com.codeleopard.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 得到登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改当前用户密码
    public int updatePwd(Connection connection, int id, String passpord) throws SQLException;

    // 根据用户名或者角色码查询用户总数
    public int getUserCount(Connection connection, String userName, int userRole) throws Exception;

    // 通过条件查询userList
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws Exception;


}
