package com.lea.task;

import com.lea.preparedstatement3.util.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 批量数据插入操作
 * @author lzc
 * @create 2020/09/20 下午 3:27
 */
public class InsertTest {

    @Test
    public void insert1() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        Long start = System.currentTimeMillis();
        for (int i = 1; i <= 20000; i++) {
            ps.setObject(1, "name_" + 1);
            ps.execute();
        }
        Long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
        JDBCUtil.close(conn, ps);
    }

    /*
    批量插入
        addBatch() executeBatch() clearBatch()

     1，mysql 默认服务器是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持，
     ?rewriteBatchedStatements=true 拼接到url后面
     2. 使用更新后的mysql驱动 mysql-connector-java-5.1.37-bin.jar

     3. 批量插入， sql 语法用 insert into table(name) values(value).
            别用 value
     */
    @Test
    public void insert2() throws Exception {
        Connection conn = JDBCUtil.getConnection();
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        Long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            ps.setObject(1, "name_" + 1);
            // 条件到批量语句中
            ps.addBatch();
            if (i % 500 == 0) {
                // 执行批量语句
                ps.executeBatch();
                // 清楚批量语句
                ps.clearBatch();
            }
        }
        Long end = System.currentTimeMillis();
        System.out.println((end - start));
        JDBCUtil.close(conn, ps);
    }

    /*
    批量插入 四, 关闭自动提交，自定义提交
     */
    @Test
    public void insert3() throws Exception {
        Connection conn = JDBCUtil.getConnection();

        // 设置不允许自动提交
        conn.setAutoCommit(false);
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        Long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            ps.setObject(1, "name_" + 1);
            // 条件到批量语句中
            ps.addBatch();
            if (i % 500 == 0) {
                // 执行批量语句
                ps.executeBatch();
                // 清楚批量语句
                ps.clearBatch();
            }
        }
        conn.commit();
        Long end = System.currentTimeMillis();
        System.out.println((end - start));
        JDBCUtil.close(conn, ps);
    }


}
