package com.alibaba.druid.sumkor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
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
    }
}
