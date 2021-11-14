package com.codeleopard.service.user;

import com.codeleopard.dao.BaseDao;
import com.codeleopard.dao.user.UserDao;
import com.codeleopard.dao.user.UserDaoImpl;
import com.codeleopard.pojo.User;
import org.junit.Test;


import java.sql.Connection;
import java.sql.SQLException;

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
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BaseDao.closeResource(connection, null, null);
        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        System.out.println("UserServiceImpl: " + pwd);

        Connection connection = null;
        boolean flag = false;

        connection = BaseDao.getConnection();
        // 修改密码
        try {
            if(userDao.updatePwd(connection, id, pwd) > 0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 2);
        System.out.println(userCount);
    }
}
