package com.lea.dbcp;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPTest {

    /*
    测试DBCP数据库连接池技术
     */
    @Test
    public void test() throws SQLException {
        // 创建DBCP数据库连接池
        BasicDataSource dataSource = new BasicDataSource();
        // 设置基本信息
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 连接池管理属性
//        1.initialSize ：连接池启动时创建的初始化连接数量（默认值为0）
        dataSource.setInitialSize(10);
//        2.maxActive ：连接池中可同时连接的最大的连接数（默认值为8，调整为20，高峰单机器在20并发左右，自己根据应用场景定）
        dataSource.setMaxActive(20);
//        3.maxIdle：连接池中最大的空闲的连接数，超过的空闲连接将被释放，如果设置为负数表示不限制（默认为8个，maxIdle不能设置太小，因为假如在高负载的情况下，连接的打开时间比关闭的时间快，会引起连接池中idle的个数 上升超过maxIdle，而造成频繁的连接销毁和创建，类似于jvm参数中的Xmx设置)
//
//        4.minIdle：连接池中最小的空闲的连接数，低于这个数量会被创建新的连接（默认为0，调整为5，该参数越接近maxIdle，性能越好，因为连接的创建和销毁，都是需要消耗资源的；但是不能太大，因为在机器很空闲的时候，也会创建低于minidle个数的连接，类似于jvm参数中的Xmn设置）
//
//        5.maxWait  ：最大等待时间，当没有可用连接时，连接池等待连接释放的最大时间，超过该时间限制会抛出异常，如果设置-1表示无限等待（默认为无限，调整为60000ms，避免因线程池不够用，而导致请求被无限制挂起）
//
//        6.poolPreparedStatements：开启池的prepared（默认是false，未调整，经过测试，开启后的性能没有关闭的好。）
//
//        7.maxOpenPreparedStatements：开启池的prepared 后的同时最大连接数（默认无限制，同上，未配置）
//
//        8.minEvictableIdleTimeMillis  ：连接池中连接，在时间段内一直空闲， 被逐出连接池的时间
//
//        9.removeAbandonedTimeout  ：超过时间限制，回收没有用(废弃)的连接（默认为 300秒，调整为180）
//
//        10.removeAbandoned  ：超过removeAbandonedTimeout时间后，是否进 行没用连接（废弃）的回收（默认为false，调整为true)


        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    /*
    方式二： 使用配置文件 测试DBCP数据库连接池技术
    */
    @Test
    public void test1() throws Exception {
        // 通过配置文件，创建DBCP数据库连接池
        Properties properties = new Properties();
        InputStream is = null;
        // 方式一：
//        is = new FileInputStream("dbcp.properties");
        // 方式二： 通过类加载器
        is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(is);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);


        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
