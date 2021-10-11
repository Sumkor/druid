package com.sumkor.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Druid连接池介绍
 * https://github.com/alibaba/druid/wiki/Druid%E8%BF%9E%E6%8E%A5%E6%B1%A0%E4%BB%8B%E7%BB%8D
 *
 * DruidDataSource配置属性列表
 * https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
 *
 * @see com.alibaba.druid.pool.DruidAbstractDataSource
 * @see com.alibaba.druid.pool.DruidDataSource
 * @see com.alibaba.druid.pool.DruidPooledConnection
 *
 * @author Sumkor
 * @since 2021/9/1
 */
public class ConnectTest {

    private DruidDataSource dataSource;

    @Before
    public void init() {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb?serverTimezone=UTC");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        dataSource.setMinEvictableIdleTimeMillis(10);
        dataSource.setTimeBetweenEvictionRunsMillis(10);
    }

    @Test
    public void test() throws SQLException {
        DruidPooledConnection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from t_student");
        boolean execute = preparedStatement.execute();
        System.out.println("execute = " + execute);
        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            System.out.println("id:" + resultSet.getInt("id") + " address:" + resultSet.getString(2) + " name:" + resultSet.getString(4));
        }
        /**
         * 获取连接
         * @see com.alibaba.druid.pool.DruidDataSource#getConnection()
         * @see com.alibaba.druid.pool.DruidDataSource#getConnection(long)
         *
         * 1. 初始化
         * @see com.alibaba.druid.pool.DruidDataSource#init()
         *
         * 这里会向 JDBC 的 DriverManager 注册 DruidDriver 驱动 TODO DruidDriver 有什么用？
         * @see com.alibaba.druid.proxy.DruidDriver#getInstance()
         * @see com.alibaba.druid.proxy.DruidDriver#registerDriver(java.sql.Driver)
         */
    }

}
