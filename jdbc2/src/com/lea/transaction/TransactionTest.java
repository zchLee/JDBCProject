package com.lea.transaction;

import com.lea.preparedstatement3.util.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 事务：一组逻辑操作单元，使数据从一种状态，变换成另一种状态
 *      一组逻辑操作单元，一个或多个DML操作。
 * 事务处理原则：
 *      保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，
 *      要么所有的事务都被提交(commit)，那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，
 *      整个事务回滚(rollback)到最初状态。
 *
 * 数据一旦提交，就不可回滚
 *
 *  什么操作会自动提交
 *      > DDL
 *      > DML 默认自动提交
 *          > 可以关闭自动提交，手动提交 set autocommit = false
 *      > 默认关闭mysql连接时，会自动提交
 * @author lzc
 * @create 2020/09/20 下午 4:39
 */
public class TransactionTest {

    @Test
    public void test() {
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            // 设置不自动提交
            conn.setAutoCommit(false);
            // 设置当前连接的隔离级别， 在框架会用到
//            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            String sql1 = "update account set balance = balance - 100 where username = ?";
            update(conn, sql1, "john");
            int a = 10 / 0;
            String sql2 = "update account set balance = balance + 100 where username = ?";
            update(conn, sql2, "rose");
            // 全部执行完成，提交事务
            conn.commit();
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try {
                // 执行异常，回滚数据到初始状态
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                // 防止其他线程使用，连接时事务默认值改变--使用与连接池
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtil.close(conn, null);
        }
    }

    /*
        不关闭连接，防止事务提交
     */
    public static int update(Connection conn, String sql, Object ...args) {
        PreparedStatement ps = null;
        try {
            // 获取数据库连接
            conn = JDBCUtil.getConnection();
            // 预编译sql，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
//            System.out.println(ps.);
            // execute执行，有返回结果集，true，没有返回结果时false
            return ps.executeUpdate();
//            executeUpdate() 执行修改操作。返回影响行数
            // executeQuery() 执行查询操作。返回结果集
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.close(null, ps);
        }
        return 0;
    }
}
