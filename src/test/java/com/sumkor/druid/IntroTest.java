package com.sumkor.druid;

/**
 * @author Sumkor
 * @since 2021/10/11
 */
public class IntroTest {

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
     * TODO
     *
     * 采集监控信息，包括 SQL 执行、并发、慢查、执行时间区间分布等
     * @see com.alibaba.druid.filter.stat.StatFilter
     *
     * 防 SQL 注入
     * @see com.alibaba.druid.wall.WallFilter
     */
}
