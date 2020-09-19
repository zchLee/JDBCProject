package com.lea.preparedstatement3.curd;

import com.lea.connection1.ConnectionTest;
import com.lea.preparedstatement3.util.JDBCUtil;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 使用PreparedStatement替换Statement，实现增删改查
 *
 * @author lzc
 * @create 2020/09/18 下午 3:17
 */
public class PreparedStatementTest {

    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = ConnectionTest.getConnection();
            // ? 站位符
            String sql = "INSERT INTO customers(`name`,email,birth) VALUES (?,?,?)";
            // 预编译sql,创建PreparedStatement
            preparedStatement = conn.prepareStatement(sql);
            // 填充站位符，字段下标从1开始
            preparedStatement.setString(1, "");
            preparedStatement.setString(2, "");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1110-01-11");
            preparedStatement.setDate(3, new Date(date.getTime()));

            // 执行sql
            preparedStatement.execute();
            // 关闭连接
            preparedStatement.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, preparedStatement);
        }

    }
}
