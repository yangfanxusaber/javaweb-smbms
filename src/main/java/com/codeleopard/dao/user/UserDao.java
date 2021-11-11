package com.codeleopard.dao.user;

import com.codeleopard.pojo.User;

import java.sql.Connection;

public interface UserDao {
    // 得到登录的用户
    public User getLoginUser(Connection connection, String userCode);
}
