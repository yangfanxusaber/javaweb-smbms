package com.codeleopard.service.user;

import com.codeleopard.dao.BaseDao;
import com.codeleopard.dao.user.UserDao;
import com.codeleopard.dao.user.UserDaoImpl;
import com.codeleopard.pojo.User;
import org.junit.Test;


import java.sql.Connection;

public class UserServiceImpl implements UserService{

    // 业务层都会调用dao层，所以我们要引入Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        // 通过业务层调用对应的具体的数据库操作
        user = userDao.getLoginUser(connection, userCode);
        BaseDao.closeResource(connection, null, null);
        return user;
    }

//    @Test
//    public void test(){
//        UserServiceImpl userService = new UserServiceImpl();
//        User admin = userService.login("admin", "1234qe567");
//        System.out.println(admin.getUserPassword());
//    }
}
