package com.alibaba.druid.sumkor;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

import java.util.List;

/**
 * @author Sumkor
 * @since 2021/9/1
 */
public class SqlAst {

    /**
     * SQL Parser
     * https://github.com/alibaba/druid/wiki/SQL-Parser
     *
     * SQL Parser是Druid的一个重要组成部分，Druid内置使用SQL Parser来实现防御SQL注入（WallFilter）、合并统计没有参数化的SQL(StatFilter的mergeSql)、SQL格式化、分库分表。
     * parser是将输入文本转换为ast（抽象语法树），parser有包括两个部分，Parser和Lexer，其中Lexer实现词法分析，Parser实现语法分析。
     *
     * @see com.alibaba.druid.sql.parser.SQLParser
     * @see com.alibaba.druid.sql.parser.Lexer
     */

    /**
     * AST
     * https://github.com/alibaba/druid/wiki/Druid_SQL_AST
     *
     * AST是abstract syntax tree的缩写，也就是抽象语法树。和所有的Parser一样，Druid Parser会生成一个抽象语法树。
     * AST是parser输出的结果。下面是获得抽象语法树的一个例子：
     */
    @Test
    public void ast() {
        DbType dbType = JdbcConstants.MYSQL;
        String sql = "select * from t_student";
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
        System.out.println("statementList = " + statementList);

        /**
         * Visitor是遍历AST的手段，是处理AST最方便的模式，Visitor是一个接口，有缺省什么都没做的实现VistorAdapter。
         * Druid内置提供了OutputVisitor用来把AST输出为字符串
         */
        String str = SQLUtils.toSQLString(statementList, dbType);
        System.out.println("str = " + str);
    }

    /**
     * SchemaRepository
     * https://github.com/alibaba/druid/wiki/SQL_Schema_Repository
     *
     * Druid SQL Parser内置了一个SchemaRepository，在内存中缓存SQL Schema信息，用于SQL语义解析中的ColumnResolve等操作。
     *
     * 测试用例：
     * @see com.alibaba.druid.bvt.sql.mysql.create.MySqlCreateTable_showColumns_repository_test
     * @see com.alibaba.druid.bvt.sql.mysql.create.MySqlCreateTable_showColumns_test
     *
     * 转换成字符串的代码：
     * @see com.alibaba.druid.sql.dialect.mysql.visitor.MySqlShowColumnOutpuVisitor
     */
    @Test
    public void schema() {
        SchemaRepository repository = new SchemaRepository(JdbcConstants.MYSQL);
        String sql = "";
        repository.console(sql);
    }

    /**
     * Druid SQL Parser提供了格式化代码的工具类。这个是基于语义分析做的SQL格式化功能，比其他的SQL格式化做的更智能，效果更好。
     * https://github.com/alibaba/druid/wiki/SQL_Format
     */
    @Test
    public void sqlFormat() {
        String sql = "update t set name = 'x' where id < 100 limit 10";
        String result = SQLUtils.format(sql, JdbcConstants.MYSQL);
        System.out.println(result); // 缺省大写格式

        String result_lcase = SQLUtils.format(sql
                , JdbcConstants.MYSQL
                , SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
        System.out.println(result_lcase); // 小写格式
        /**
         * -- 这是缺省的大写格式
         * UPDATE t
         * SET name = 'x'
         * WHERE id < 100
         * LIMIT 10
         *
         * -- 这是小写格式
         * update t
         * set name = 'x'
         * where id < 100
         * limit 10
         */
    }
}
