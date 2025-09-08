package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.domain.form.CodegenTableForm;
import com.yeeiee.domain.vo.CodegenTableVo;
import com.yeeiee.mapper.CodegenTableMapper;
import com.yeeiee.service.CodegenTableService;
import com.yeeiee.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

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
    public List<CodegenTableVo> getTableList(Long dataSourceId, String name, String comment) {
        val dataSourceConfig = dataSourceService.getById(dataSourceId);

        // 移除在 Codegen 中，已经存在的
        List<String> existsTables = codegenTableMapper.getExistsTableNameList(dataSourceId);

        val tableInfoList = this.getTableList(dataSourceConfig, (strategyConfigBuilder) -> {
            if (!CollectionUtils.isEmpty(existsTables)) {
                strategyConfigBuilder.addExclude(existsTables);
            }
            if (StringUtils.hasText(name)) {
                strategyConfigBuilder.likeTable(new LikeTable(name));
            }
        });

        return tableInfoList.stream().filter(tableInfo ->
                        (!StringUtils.hasText(comment) || tableInfo.getComment().contains(comment))
                )
                .map(tableInfo -> {
                    val codegenTableVo = new CodegenTableVo();
                    codegenTableVo.setName(tableInfo.getName());
                    codegenTableVo.setComment(tableInfo.getComment());
                    return codegenTableVo;
                })
                .sorted(Comparator.comparing(CodegenTableVo::getName))
                .toList();
    }

    @Override
    public void saveCodegenTable(CodegenTableForm codegenTableForm) {
        val dataSourceId = codegenTableForm.getDataSourceId();
        val dataSourceConfig = dataSourceService.getById(dataSourceId);

        val tableInfoList = this.getTableList(dataSourceConfig, (strategyConfigBuilder) -> {
            val tableNames = codegenTableForm.getTableNames();
            strategyConfigBuilder.addInclude(tableNames);

            val ignoreTablePrefix = codegenTableForm.getIgnoreTablePrefix();
            if (StringUtils.hasText(ignoreTablePrefix)) {
                strategyConfigBuilder.addTablePrefix(ignoreTablePrefix);
            }

            val ignoreColumnPrefix = codegenTableForm.getIgnoreColumnPrefix();
            if (StringUtils.hasText(ignoreColumnPrefix)) {
                strategyConfigBuilder.addFieldPrefix(ignoreColumnPrefix);
            }
        });

        System.out.println("tableInfoList = " + tableInfoList);
    }

    private List<TableInfo> getTableList(DataSource dataSource, Consumer<StrategyConfig.Builder> consumer) {
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

        val globalConfigBuilder = new GlobalConfig.Builder();
        // 只使用 LocalDateTime 类型
        globalConfigBuilder.dateType(DateType.TIME_PACK).build();

        // 额外策略
        consumer.accept(strategyConfigBuilder);

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
