package com.alibaba.druid.sumkor;

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
 * @see com.alibaba.druid.pool.DruidPooledConnection
 * @see com.alibaba.druid.pool.DruidDataSource
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
         */
    }

    /**
     * 当网络断开或者数据库服务器Crash时，连接池里面会存在“不可用连接”，连接池需要一种机制剔除这些“不可用连接”。
     * 在Druid和JBoss连接池中，剔除“不可用连接”的机制称为ExceptionSorter，实现的原理是根据异常类型/Code/Reason/Message来识别“不可用连接”。
     * 没有类似ExceptionSorter的连接池，在数据库重启或者网络中断之后，不能恢复工作，所以ExceptionSorter是连接池是否稳定的重要标志。
     *
     * 在Druid中，会根据连接池连接数据库的类型自动匹配不同类型的ExceptionSorter，不需要额外配置。这一点Druid和JBoss是不同的。
     *
     * @see com.alibaba.druid.pool.vendor.MySqlExceptionSorter
     */

    /**
     * 采集监控信息，包括 SQL 执行、并发、慢查、执行时间区间分布等
     * @see com.alibaba.druid.filter.stat.StatFilter
     *
     * 防 SQL 注入
     * @see com.alibaba.druid.wall.WallFilter
     */
}
