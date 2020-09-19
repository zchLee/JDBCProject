package com.lea.preparedstatement3.curd;

import com.lea.preparedstatement3.bean.Customers;
import com.lea.preparedstatement3.util.JDBCUtil;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询
 *
 * @author lzc
 * @create 2020/09/19 下午 1:46
 */
public class preparedStatementQueryTest {

    public static void main(String[] args) throws Exception {
//        String sql = "select id,name,email,birth from customers where id = ?";
//        Customers customers = query(Customers.class, sql, 1);
//        System.out.println(customers);
        String sql = "select id,name,email,birth from customers where id > ?";
        List<Customers> customers = queryList(Customers.class, sql, 1);
        customers.forEach(System.out::println);
    }


    /*
    获取表中的一条记录
     */
    public static <T> List<T> queryList(Class<T> clazz, String sql, Object ...args) throws Exception {
        Connection conn = JDBCUtil.getConnection();
        // 预编译sql
        PreparedStatement ps = conn.prepareStatement(sql);
        // 设置参数
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
        // 执行查询
        ResultSet rs = ps.executeQuery();
        List<T> list = new ArrayList<>(rs.getRow());
        // 结果集的元数据
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            // 获得对象
            T t = clazz.newInstance();
            for (int i = 0; i < columnCount; i++) {
                // 列名
                String columnLabel = metaData.getColumnLabel(i + 1);
                // 列值
                Object columnValue = rs.getObject(i + 1);
                // getColumnClassName(i) 获取列名对应的java类型名
//                String columnClassName = metaData.getColumnClassName(i + 1);
                Field field = clazz.getDeclaredField(columnLabel);
                field.setAccessible(true);
                field.set(t, columnValue);
            }
            list.add(t);
        }
        return list;
    }

    /*
    获取表中的一条记录
     */
    public static <T> T query(Class<T> clazz, String sql, Object ...args) throws Exception {
        Connection conn = JDBCUtil.getConnection();
        // 预编译sql
        PreparedStatement ps = conn.prepareStatement(sql);
        // 设置参数
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
        // 执行查询
        ResultSet rs = ps.executeQuery();
        // 结果集的元数据
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        if (rs.next()) {
            // 获得对象
            T t = clazz.newInstance();
            for (int i = 0; i < columnCount; i++) {
                // 列名
                String columnLabel = metaData.getColumnLabel(i + 1);
                // 列值
                Object columnValue = rs.getObject(i + 1);
                // getColumnClassName(i) 获取列名对应的java类型名
//                String columnClassName = metaData.getColumnClassName(i + 1);
                Field field = clazz.getDeclaredField(columnLabel);
                field.setAccessible(true);
                field.set(t, columnValue);
            }
            return t;
        }
        return null;
    }
}
