package com.yeeiee.codegen.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.yeeiee.codegen.domain.dto.CodegenTableConfigDto;
import com.yeeiee.codegen.domain.entity.CodegenColumn;
import com.yeeiee.codegen.domain.entity.CodegenTable;
import com.yeeiee.codegen.domain.entity.DataSource;
import com.yeeiee.codegen.domain.form.CodegenTableColumnsForm;
import com.yeeiee.codegen.domain.form.CodegenTableImportForm;
import com.yeeiee.codegen.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.codegen.domain.vo.CodegenTableVo;
import com.yeeiee.codegen.exception.CodegenFailedException;
import com.yeeiee.codegen.mapper.CodegenTableMapper;
import com.yeeiee.codegen.service.CodegenColumnService;
import com.yeeiee.codegen.service.CodegenTableService;
import com.yeeiee.codegen.service.DataSourceService;
import com.yeeiee.utils.BeanUtil;
import com.yeeiee.utils.CodegenUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    private final CodegenColumnService codegenColumnService;

    @Override
    public List<CodegenTableSelectorVo> getCodegenTableSelector(Long dataSourceId) {
        val dataSourceConfig = dataSourceService.getById(dataSourceId);
        val existsTableNameList = this.lambdaQuery()
                .select(CodegenTable::getTableName)
                .eq(CodegenTable::getDataSourceId, dataSourceConfig.getId())
                .list()
                .stream()
                .map(CodegenTable::getTableName)
                .toList();

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
            throw new CodegenFailedException("数据源连接异常: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Override
    public void addCodegenTable(CodegenTableImportForm codegenTableImportForm) {
        val dataSourceId = codegenTableImportForm.getDataSourceId();
        val dataSourceConfig = dataSourceService.getById(dataSourceId);

        val codegenTableConfigDto = new CodegenTableConfigDto();
        codegenTableConfigDto.setIncludeTableName(codegenTableImportForm.getTableName());
        codegenTableConfigDto.setIgnoreTablePrefix(codegenTableImportForm.getIgnoreTablePrefix());
        codegenTableConfigDto.setIgnoreColumnPrefix(codegenTableImportForm.getIgnoreColumnPrefix());

        val tableInfoList = this.getTableList(dataSourceConfig, codegenTableConfigDto);

        this.saveCodegenTable(
                tableInfoList,
                (tableInfo) -> CodegenUtil.toCodegenTable(tableInfo, codegenTableImportForm)
        );

    }

    @Override
    public void modifyCodegenColumnList(Long id) {
        val codegenTable = this.getById(id);
        if (codegenTable == null) {
            throw new CodegenFailedException("表不存在: " + id);
        }
        val dataSourceConfig = dataSourceService.getById(codegenTable.getDataSourceId());
        if (dataSourceConfig == null) {
            throw new CodegenFailedException("数据源不存在: " + codegenTable.getDataSourceId());
        }

        val codegenTableConfigDto = new CodegenTableConfigDto();
        codegenTableConfigDto.setIncludeTableName(codegenTable.getTableName());
        codegenTableConfigDto.setIgnoreTablePrefix(codegenTable.getIgnoreTablePrefix());
        codegenTableConfigDto.setIgnoreColumnPrefix(codegenTable.getIgnoreColumnPrefix());

        val tableInfoList = this.getTableList(dataSourceConfig, codegenTableConfigDto);

        this.saveCodegenTable(
                tableInfoList,
                (tableInfo) -> codegenTable
        );
    }

    @Override
    public IPage<CodegenTableVo> getCodegenTablePage(Page<CodegenTableVo> page, Wrapper<CodegenTable> wrapper) {
        return codegenTableMapper.selectCodegenTablePage(page, wrapper);
    }

    @Override
    public void modifyCodegenConfig(CodegenTableColumnsForm codegenTableColumnsForm) {
        val codegenTable = BeanUtil.toBean(codegenTableColumnsForm.getTable(), CodegenTable.class);
        this.updateById(codegenTable);
        val codegenColumnList = BeanUtil.toBean(codegenTableColumnsForm.getColumns(), CodegenColumn.class);
        codegenColumnService.updateBatchById(codegenColumnList);
    }

    /**
     * 保存代码生成表记录
     *
     * @param tableInfoList table info list
     * @param function      返回一个codegen table
     */
    private void saveCodegenTable(List<TableInfo> tableInfoList, Function<TableInfo, CodegenTable> function) {
        for (TableInfo tableInfo : tableInfoList) {
            CodegenUtil.validateTableInfo(tableInfo);
            val codegenTable = function.apply(tableInfo);

            // id不存在才插入
            var id = codegenTable.getId();
            if (id == null) {
                this.save(codegenTable);

                // 如果是更新，需要删除之前的列，更新时间
            } else {
                val updateCodegenTable = new CodegenTable();
                updateCodegenTable.setId(id);
                this.updateById(updateCodegenTable);
                codegenColumnService.remove(
                        Wrappers.<CodegenColumn>lambdaQuery()
                                .eq(CodegenColumn::getTableId, id)
                );
            }

            id = codegenTable.getId();
            val fields = tableInfo.getFields();
            val codegenColumnList = CodegenUtil.toCodegenColumnList(id, fields);
            codegenColumnService.saveBatch(codegenColumnList);
        }
    }

    /**
     * 获取 TableInfo，虽然返回的是 List，但是肯定是一条记录
     *
     * @param dataSource            数据源配置
     * @param codegenTableConfigDto 代码生成配置dto
     * @return TableInfo
     */
    private List<TableInfo> getTableList(DataSource dataSource, CodegenTableConfigDto codegenTableConfigDto) {
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
        // 自定义类型转换
        dataSourceConfigBuilder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
            if (typeCode == Types.TINYINT) {
                return DbColumnType.INTEGER;
            }
            if (typeCode == Types.SMALLINT) {
                return DbColumnType.INTEGER;
            }

            return typeRegistry.getColumnType(metaInfo);
        });

        val strategyConfigBuilder = new StrategyConfig.Builder();
        // 忽略视图
        strategyConfigBuilder.enableSkipView();
        // 过滤表名
        strategyConfigBuilder.addInclude(codegenTableConfigDto.getIncludeTableName());
        // 忽略表前缀
        val ignoreTablePrefix = codegenTableConfigDto.getIgnoreTablePrefix();
        if (StringUtils.hasText(ignoreTablePrefix)) {
            strategyConfigBuilder.addTablePrefix(ignoreTablePrefix);
        }
        // 忽略字段前缀
        val ignoreColumnPrefix = codegenTableConfigDto.getIgnoreColumnPrefix();
        if (StringUtils.hasText(ignoreColumnPrefix)) {
            strategyConfigBuilder.addFieldPrefix(ignoreColumnPrefix);
        }

        val globalConfigBuilder = new GlobalConfig.Builder();
        // 只使用 LocalDateTime 类型
        globalConfigBuilder.dateType(DateType.TIME_PACK).build();

        val builder = new ConfigBuilder(
                null,
                dataSourceConfigBuilder.build(),
                strategyConfigBuilder.build(),
                null,
                globalConfigBuilder.build(),
                null);

        return builder.getTableInfoList();
    }
}
