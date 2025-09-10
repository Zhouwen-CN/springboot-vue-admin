package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.exception.DataSourceConnectException;
import com.yeeiee.mapper.CodegenTableMapper;
import com.yeeiee.service.CodegenTableService;
import com.yeeiee.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 代码生成表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
@RequiredArgsConstructor
@Service
public class CodegenTableServiceImpl extends ServiceImpl<CodegenTableMapper, CodegenTable> implements CodegenTableService {

    private final DataSourceService dataSourceService;
    private final CodegenTableMapper codegenTableMapper;

    @Override
    public List<CodegenTableSelectorVo> getTableList(Long dataSourceId) {
        val dataSourceConfig = dataSourceService.getById(dataSourceId);
        val existsTableNameList = codegenTableMapper.getExistsTableNameList(dataSourceId);
        try (val connection = DriverManager.getConnection(dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword())) {

            val metaData = connection.getMetaData();
            val resultSet = metaData.getTables(
                    connection.getCatalog(),
                    connection.getSchema(),
                    null,
                    new String[]{"TABLE"}
            );

            val codegenTableVoList = new ArrayList<CodegenTableSelectorVo>();
            while (resultSet.next()) {
                val tableName = resultSet.getString("TABLE_NAME");
                val tableComment = resultSet.getString("REMARKS");
                if (existsTableNameList.contains(tableName)) {
                    continue;
                }
                val codegenTableSelectorVo = new CodegenTableSelectorVo();
                codegenTableSelectorVo.setName(tableName);
                codegenTableSelectorVo.setComment(tableComment);
                codegenTableVoList.add(codegenTableSelectorVo);
            }
            return codegenTableVoList;
        } catch (SQLException e) {
            throw new DataSourceConnectException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Override
    public void addCodegenTable(CodegenTableImportForm codegenTableImportForm) {
        val dataSourceId = codegenTableImportForm.getDataSourceId();
        val dataSourceConfig = dataSourceService.getById(dataSourceId);

        val tableInfoList = this.getTableList(dataSourceConfig, codegenTableImportForm);

        System.out.println("tableInfoList = " + tableInfoList);
    }

    private List<TableInfo> getTableList(DataSource dataSource, CodegenTableImportForm codegenTableImportForm) {
        val url = dataSource.getUrl();
        val username = dataSource.getUsername();
        val password = dataSource.getPassword();

        val dataSourceConfigBuilder = new DataSourceConfig.Builder(
                url,
                username,
                password
        );
        // 特殊：SQLServer jdbc 非标准，参见 https://github.com/baomidou/mybatis-plus/issues/5419
        val dbType = JdbcUtils.getDbType(url);
        if (DbType.SQL_SERVER == dbType || DbType.SQL_SERVER2005 == dbType) {
            dataSourceConfigBuilder.databaseQueryClass(SQLQuery.class);
        }

        val strategyConfigBuilder = new StrategyConfig.Builder();
        // 忽略视图
        strategyConfigBuilder.enableSkipView();
        // 过滤表名
        val tableNames = codegenTableImportForm.getTableNames();
        strategyConfigBuilder.addInclude(tableNames);
        // 忽略表前缀
        val ignoreTablePrefix = codegenTableImportForm.getIgnoreTablePrefix();
        if (StringUtils.hasText(ignoreTablePrefix)) {
            strategyConfigBuilder.addTablePrefix(ignoreTablePrefix);
        }
        // 忽略字段前缀
        val ignoreColumnPrefix = codegenTableImportForm.getIgnoreColumnPrefix();
        if (StringUtils.hasText(ignoreColumnPrefix)) {
            strategyConfigBuilder.addFieldPrefix(ignoreColumnPrefix);
        }

        val globalConfigBuilder = new GlobalConfig.Builder();
        // 只使用 LocalDateTime 类型
        globalConfigBuilder.dateType(DateType.TIME_PACK).build();

        ConfigBuilder builder = new ConfigBuilder(
                null,
                dataSourceConfigBuilder.build(),
                strategyConfigBuilder.build(),
                null,
                globalConfigBuilder.build(),
                null);

        return builder.getTableInfoList();
    }
}
