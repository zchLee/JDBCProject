package com.lea.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/*
    Druid获取数据库连接池
 */
public class DruidTest {

    /*
    通过配置文件配置druid数据库连接池
     */
    @Test
    public void test() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        Properties pro = new Properties();
        pro.load(is);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(pro);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

}
