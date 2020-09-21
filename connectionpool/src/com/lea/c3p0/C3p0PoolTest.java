package com.lea.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;

/**
 * @author lzc
 * @create 2020/09/21 上午 11:44
 */
public class C3p0PoolTest {

    /*
    方式一：
     */
    @Test
    public void test() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("root");
        // 通过设置相关的参数，对数据库连接池进行管理
        // 设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);
        Connection conn = cpds.getConnection();
        System.out.println(conn);

        // 销毁连接池
//        DataSources.destroy(cpds);
    }

    /*
    方式二： 使用配置文件
     */
    @Test
    public void test1() throws Exception {
        // 配置名
        ComboPooledDataSource c3p0 = new ComboPooledDataSource("c3p0");
        Connection conn = c3p0.getConnection();
        System.out.println(conn);

    }
}
