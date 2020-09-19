package com.lea.preparedstatement3.curd;

import com.lea.preparedstatement3.util.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author lzc
 * @create 2020/09/18 下午 5:12
 */
public class preparedStatementUpdateTest {

    @Test
    public void test3() {
//        String sql = "delete from customers where id = ?";
//        update(sql, 3);

        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", 2);
    }

    /**
     * 通用增删改
     * @author: lzc
     * @date: 2020/09/18 下午 5:30
     * @param sql
     * @param args  sql中占位符要和此数组长度一致
     * @return: void
     */
    public void update(String sql, Object ...args) {
        // 检查参数和sql中占位符
        check(sql, args);

        Connection conn = null;
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
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtil.close(conn, ps);
        }
    }

    @Test
    public void test1() throws SQLException, IOException, ClassNotFoundException {
        // 获取数据库连接
        Connection conn = JDBCUtil.getConnection();
        String sql = "update customers set name = ? where id = ?";
        // 预编译sql，返回PreparedStatement实例
        PreparedStatement ps = conn.prepareStatement(sql);
        // 根据占位符，设置参数
        ps.setObject(1, "张学友");
        ps.setObject(2, 18);
        // 执行sql
        boolean execute = ps.execute();
        System.out.println(execute);
        System.out.println(execute ? "修改成功" : "修改失败");
        // 关闭资源
        JDBCUtil.close(conn, ps);
    }

    /*
        检查sql中占位符与参数个数是否一致
     */
    public void check (String str, Object ...args) {
        if (null == str || str.trim().length() == 0)
            throw new RuntimeException("sql为空");

        // 返回字符串中?出现的次数
        long count = str.chars().filter(c -> '?' == c).count();

        if (null != args && count != args.length)
            throw new RuntimeException("参数不匹配");
    }
}
