package com.lea.commonsdbutils;

import com.lea.preparedstatement3.bean.Customers;
import com.lea.utils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import sun.plugin2.main.server.ResultHandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*
commons-dbutils是Apache 组织提供的一个开源的JDBC工具类库，封装了针对于数据库的增删改查操作

以下异常需要try catch finally 操作
 */
public class QueryRunnerTest {

    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getDruidConnection();
            String sql = "insert into customers(name, email, birth) values(?,?,?)";
            int i = runner.update(conn, sql, "张三", "zhangsan@163.com", new Date(1231831L));
            System.out.println(i);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 通过DbUtils关闭连接
            try {
                DbUtils.close(conn);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    查询单条
     BeanHandler 封装单条记录到javaBean类中
     */
    @Test
    public void test2() throws SQLException {
        Connection conn = JDBCUtils.getDruidConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "select id,name,email,birth from customers where id = ?";
        BeanHandler<Customers> handler = new BeanHandler<>(Customers.class);
        Customers customers = runner.query(conn, sql, handler, 7);
        System.out.println(customers);
        // 通过DbUtils关闭连接 帮try catch
        DbUtils.closeQuietly(conn);
    }

    /*
    查询单条
     BeanListHandler 封装多条记录到javaBean类中
     */
    @Test
    public void test3() throws SQLException {
        Connection conn = JDBCUtils.getDruidConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "select id,name,email,birth from customers where id > ?";
        BeanListHandler<Customers> handler = new BeanListHandler<>(Customers.class);
        List<Customers> customers = runner.query(conn, sql, handler, 7);
        customers.forEach(System.out::println);
        // 通过DbUtils关闭连接
        DbUtils.closeQuietly(conn);
    }

    /*
    查询单条
     MapHandler 封装单条记录到Map中

     MapListHandler : 封装多条Map，并返回List<Map>
     */
    @Test
    public void test4() throws SQLException {
        Connection conn = JDBCUtils.getDruidConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "select id,name,email,birth from customers where id = ?";
        MapHandler mapHandler = new MapHandler();
        Map map = runner.query(conn, sql, mapHandler, 7);
        System.out.println(map);
        // 通过DbUtils关闭连接
        DbUtils.closeQuietly(conn);
    }


    /*
        查询特殊值
     */
    @Test
    public void test5() throws SQLException {
        Connection conn = JDBCUtils.getDruidConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from customers";
        ScalarHandler scalarHandler = new ScalarHandler();
        Object o = runner.query(conn, sql, scalarHandler);
        System.out.println(o);
        // 通过DbUtils关闭连接
        DbUtils.closeQuietly(conn);
    }

    /*
        自定义Handler实现类
     */
    @Test
    public void test6() throws SQLException {
        Connection conn = JDBCUtils.getDruidConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "select id from customers where id = ?";
        // 匿名实现类，jdk9及以前泛型不能省略
        ResultSetHandler<Customers> handler = new ResultSetHandler<Customers>() {
            @Override
            public Customers handle(ResultSet resultSet) throws SQLException {
                Customers customer = new Customers();
                if (resultSet.next()) {
                    customer.setId(resultSet.getInt("id"));
                }
                return customer;
            }
        };
        Customers customer = runner.query(conn, sql, handler, 3);
        System.out.println(customer);
        // 通过DbUtils关闭连接
        DbUtils.closeQuietly(conn);
    }
}
