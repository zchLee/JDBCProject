package com.lea.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    // c3p0数据库连接池，只需要造一个
    private static final ComboPooledDataSource c3p0 = new ComboPooledDataSource("c3p0");

    // dbcp数据库连接池，只需要造一个
    private static DataSource dbcp = null;
    static {
        // 通过配置文件，创建DBCP数据库连接池
        try {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(is);
            dbcp = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // druid数据库连接池
    private static DataSource druid = null;
    static {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            Properties pro = new Properties();
            pro.load(is);
            druid = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    从c3p0连接池中获取连接
     */
    public static Connection getC3p0Connection() throws SQLException {
        Connection conn = c3p0.getConnection();
        return conn;
    }

    /*
    从DBCP连接池中获取连接
     */
    public static Connection getDBCPConnection() throws SQLException {
        Connection conn = c3p0.getConnection();
        return conn;
    }

    /*
        druid连接池
     */
    public static Connection getDruidConnection() throws SQLException {
        Connection conn = druid.getConnection();
        return conn;
    }

}
