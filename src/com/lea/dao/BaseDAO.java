package com.lea.dao;

import com.lea.preparedstatement3.util.JDBCUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzc
 * @create 2020/09/20 下午 6:06
 */
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    // 获取当前BaseDAO的子类继承父类中的泛型
    {
        // this new 子类时，this指的是子类
        // 获取带泛型的父类
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        // 获取父类泛型参数类型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // 获取泛型的参数数组
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        // 强转成父类参数类型的Class实例
        clazz = (Class<T>) actualTypeArguments[0];
    }


    /*
        不关闭连接，防止事务提交
     */
    public int update(Connection conn, String sql, Object ...args) {
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

    /*
    获取表中的一条记录 ， 考虑事务的查询
     */
    public List<T> queryList(Connection conn, String sql, Object ...args) {
        // 预编译sql
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            ps = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行查询
            rs = ps.executeQuery();
            list = new ArrayList<>(rs.getRow());
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(null , ps, rs);
        }
        return list;
    }

    // 查询一条记录
    public T queryOne(Connection conn, String sql, Object ...args) throws IllegalAccessException, InstantiationException {
        List<T> list = queryList(conn, sql, args);
        if (null != list && list.size() != 0) {
            return list.get(0);
        }
        return clazz.newInstance();
    }

    /*
    查询特殊值
     */
    public <E> E getValue(Connection conn, String sql, Object ...args) {
        // 预编译sql
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行查询
            rs = ps.executeQuery();
            // 结果集的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            if (rs.next()) {
                // 获得对象
                return (E) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(null , ps, rs);
        }
        return null;
    }
}
