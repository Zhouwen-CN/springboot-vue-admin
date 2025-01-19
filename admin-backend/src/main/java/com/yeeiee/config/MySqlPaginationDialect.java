package com.yeeiee.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.yeeiee.exception.PaginationSqlParseException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * mysql 分页方言，使用 hint 来决定是否需要 limit 下压<br/>
 * 注意：<br/>
 * 1. sql 主体的 from 后面默认为主表，也就是需要分页表<br/>
 * 2. sql 主体的 from 只能是简单查询，如果有 join，需要谨慎<br/>
 *
 * @author chen
 * @since 2025-01-19
 */
public class MySqlPaginationDialect implements IDialect {
    public static final String HINT_VALUE = "UseLimitPushDown";

    @Override
    public DialectModel buildPaginationSql(String originalSql, long offset, long limit) {
        // 解析 sql
        Statement parse;
        try {
            parse = CCJSqlParserUtil.parse(originalSql);
        } catch (JSQLParserException e) {
            throw new PaginationSqlParseException(e);
        }

        /*
            是否存在 hint UseLimitPushDown:
                1. 存在 且 value相等：重构 sql，对主表进行 limit
                2. 按照之前的逻辑直接加 limit
         */
        PlainSelect plainSelect = (PlainSelect) parse;
        OracleHint oracleHint = plainSelect.getOracleHint();

        if (oracleHint != null && HINT_VALUE.equalsIgnoreCase(oracleHint.getValue())) {

            // from (select * from table) a 转换为 from (select * from table limit ?,?) a
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof ParenthesedSelect parenthesedSelect) {
                PlainSelect fromPlainSelect = parenthesedSelect.getPlainSelect();
                this.addLimit(fromPlainSelect);
            }

            /*
                1. from table a 转换为 from (select * from table limit ?,?) a
                2. from table 转换为 from (select * from table limit ?,?) table
            */
            if (fromItem instanceof Table table) {
                ParenthesedSelect parenthesedSelect = new ParenthesedSelect();

                // select
                PlainSelect fromPlainSelect = new PlainSelect();
                fromPlainSelect.addSelectItem(new AllColumns());
                fromPlainSelect.setFromItem(table);
                this.addLimit(fromPlainSelect);
                parenthesedSelect.setSelect(fromPlainSelect);

                // alias
                if (table.getAlias() == null) {
                    parenthesedSelect.setAlias(new Alias(table.getName()));
                } else {
                    parenthesedSelect.setAlias(table.getAlias());
                }

                plainSelect.setFromItem(parenthesedSelect);
            }
        } else {
            this.addLimit(plainSelect);
        }

        return (new DialectModel(plainSelect.toString(), offset, limit)).setConsumerChain();
    }

    private void addLimit(PlainSelect plainSelect) {
        Limit limitExpression = new Limit()
                .withRowCount(new JdbcParameter())
                .withOffset(new JdbcParameter());
        plainSelect.withLimit(limitExpression);
    }

}
