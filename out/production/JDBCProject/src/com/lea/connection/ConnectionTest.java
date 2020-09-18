package com.lea.connection;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.*;

/**
 * @author lzc
 * @create 2020/09/18 下午 1:28
 */
public class ConnectionTest {

    @Test
    public void test() throws SQLException {
        // 获取Driver实现类的反射
        Driver driver = new com.mysql.jdbc.Driver();
        // jdbc:mysql: 协议
        // localhost: ip
        // 3306: 端口
        // test: 所选数据库
        String url = "jdbc:mysql://localhost:3306/test";
        // 设置连接的账号密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    @Test
    public void test2() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";
        Connection conn = getConnection(driver, url, username, password);
        System.out.println(conn);
    }


    /**
     * 方式二：
     *
     * 迭代方法一获取的数据库连接
     *
     * 通过不同驱动获取数据库连接对象
     * @author: lzc
     * @date: 2020/09/18 下午 1:42
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @return: java.sql.Connection
     */
    public Connection getConnection(String driverClass, String url, String user, String password) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        // 通过反射获取Driver实现类对象：
        Class<?> clazz = Class.forName(driverClass);
        Driver driver = (Driver) clazz.newInstance();
        Properties info = new Properties();
        info.setProperty("user", user);
        info.setProperty("password", password);
        // 提供要连接的数据库
        Connection connect = driver.connect(url, info);
        return connect;
    };

    /*
     方式三：DriverManager替换Driver
     */
    @Test
    public void test3() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 获取Driver实现类的对象
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        // 注册驱动
        DriverManager.deregisterDriver(driver);
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";
        // 获取连接
        Connection conn = DriverManager.getConnection(url, username, password);

        System.out.println(conn);
    }

    /*
     方式四：优化DriverManager
     */
    @Test
    public void test4() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String url = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "root";

        // 注册驱动, mysql Driver实现类，在静态代码块中就将驱动注册到了DriverManager中
        /*
            static {
                try {
                    DriverManager.registerDriver(new Driver());
                } catch (SQLException var1) {
                    throw new RuntimeException("Can't register driver!");
                }
            }
         */
        Class.forName("com.mysql.jdbc.Driver");
        // 获取连接
        Connection conn = DriverManager.getConnection(url, username, password);

        System.out.println(conn);
    }


    /*
    方法五： 将数据库需要的连接信息声明在配置文件中，通过读取配置方式，获得声明
     */
    @Test
    public void test5() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);

        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Class.forName(driver);
        // 获取连接
        Connection conn = DriverManager.getConnection(url, username, password);

        System.out.println(conn);
    }
}
