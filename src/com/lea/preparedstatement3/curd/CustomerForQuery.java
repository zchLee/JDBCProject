package com.lea.preparedstatement3.curd;

import com.lea.preparedstatement3.bean.Customers;
import com.lea.preparedstatement3.util.JDBCUtil;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * 针对于Customer查询
 *
 * @author lzc
 * @create 2020/09/19 下午 12:53
 */
public class CustomerForQuery {

    @Test
    public void  query() throws Exception {
        String sql = "select id,name,email,birth from customers where id = ?";
        customerQuery(sql, 7);
    }

    /**
     * 针对Customers表的通用查询
     * @author: lzc
     * @date: 2020/09/19 下午 1:21
     * @param
     * @return: void
     */
    public Customers customerQuery(String sql, Object ...args) throws SQLException, IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
        // 执行查询语句
        ResultSet resultSet = ps.executeQuery();
        // 获得结果集的元数据
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 每行数据的字段个数
        int columnCount = metaData.getColumnCount();
        // 返回结果有数据，，返回true，并指针下移，如果返回false，指针不会下移
        if (resultSet.next()) {
            // 获取当前数据的各个字段值
            // 方式二：
            Customers customers = new Customers();
            for (int i = 0; i < columnCount; i++) {
                // getColumnName 获取列名，此处如果sql字段取了别名，此处还是列名
                // 推荐getColumnLabel 获取别名，如果没有设置别名，就返回列名
                String columnName = metaData.getColumnLabel(i + 1);
                Field field = Customers.class.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(customers, resultSet.getObject(i + 1));
            }
            System.out.println(customers);
            return customers;
        }
        JDBCUtil.close(conn, ps, resultSet);
        return null;
    }

    @Test
    public void query1() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = JDBCUtil.getConnection();
        String sql = "select id, name, email,birth from customers where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, 1);
        // 执行查询语句
        ResultSet resultSet = ps.executeQuery();
        // 返回结果有数据，，返回true，并指针下移，如果返回false，指针不会下移
        if (resultSet.next()) {
            // 获取当前数据的各个字段值
            // 方式一：
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date birth = resultSet.getDate(4);

            Customers customers = new Customers(id, name, email, birth);
            System.out.println(customers);

            JDBCUtil.close(conn, ps, resultSet);
        }
    }
}
