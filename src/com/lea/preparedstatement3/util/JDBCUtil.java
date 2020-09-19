package com.lea.preparedstatement3.util;

import com.lea.connection1.ConnectionTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author lzc
 * @create 2020/09/18 下午 5:05
 */
public class JDBCUtil {

    /**
     * 获取数据库连接
     * @author: lzc
     * @date: 2020/09/18 下午 5:07
     * @param
     * @return: java.sql.Connection
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        // 读取配置文件中的驱动，用户名，密码，url信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);

        String driver = properties.getProperty("driverClass");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        // 加载驱动
        Class.forName(driver);
        // 获取连接
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    };

    /**
     * 关闭连接资源
     * @author: lzc
     * @date: 2020/09/18 下午 5:09
     * @param conn
     * @param ps
     * @return: void
     */
    public static void close(Connection conn, Statement ps) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != ps) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn, Statement ps, ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

       close(conn, ps);
    }
}
