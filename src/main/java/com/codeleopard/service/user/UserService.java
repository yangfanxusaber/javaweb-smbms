package com.codeleopard.service.user;

import com.codeleopard.pojo.User;

public interface UserService {
    // 用户登录
    public User login(String userCode, String password);
}
