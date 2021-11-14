package com.codeleopard.dao.user;

import com.codeleopard.dao.BaseDao;
import com.codeleopard.pojo.User;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    // 得到当前用户
    @Override
    public User getLoginUser(Connection connection, String userCode) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if(connection != null){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};

            try {
                rs = BaseDao.execute(connection, pstm, rs, sql, params);
                if(rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                BaseDao.closeResource(null, pstm, rs);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return user;
    }

    // 修改当前用户密码
    @Override
    public int updatePwd(Connection connection, int id, String passpord) throws SQLException {
        PreparedStatement pstm = null;
        int execute = 0;
        if(connection != null){
            System.out.println("UserDaoImpl: " + passpord);

            String sql = "update smbms_user set userPassword=? where id=?";
            Object params[] = {passpord, id};
            try {
                execute = BaseDao.execute(connection, pstm, sql, params);
                BaseDao.closeResource(null, pstm, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return execute;
    }

    // 根据用户名或者角色来查询当前用户总数
    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u, smbms_role r where u.userRole = r.id");

            ArrayList<Object> list = new ArrayList<Object>();// 存放我们的参数

            if(!StringUtils.isNullOrEmpty(userName)){

                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%"); //list下标默认从0开始
            }

            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            // 怎么把list转换成数组
            Object[] params = list.toArray();
            System.out.println("UserDaoImpl->getUserCount: " + sql.toString()); // 输出最后完整的sql语句

            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            if(rs.next()){
                count = rs.getInt("count"); // 从结果集中获得的最终的数量
            }

            BaseDao.closeResource(null, pstm, rs);
        }
        return count;
    }

    // 通过条件查询userList
    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        return null;
    }
}