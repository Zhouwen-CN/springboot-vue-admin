package com.yeeiee.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.exception.CodegenFailedException;
import lombok.val;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 代码生成省工具类
 * </p>
 *
 * @author chen
 * @since 2025-09-10
 */
public final class CodegenUtil {
    private static final Set<String> IGNORE_CREATE_UPDATE_FIELDS = Set.of("createTime", "updateTime", "createUser", "updateUser");
    private static final Set<String> IGNORE_QUERY_FIELDS = Set.of("createUser", "updateUser");
    private static final Set<String> JS_NUMBER_TYPE = Set.of(
            DbColumnType.BYTE.getType(),
            DbColumnType.SHORT.getType(),
            DbColumnType.INTEGER.getType(),
            DbColumnType.LONG.getType(),
            DbColumnType.FLOAT.getType(),
            DbColumnType.DOUBLE.getType()
    );

    /**
     * 建构代码生成表
     *
     * @param tableInfo              table info
     * @param codegenTableImportForm 导入代码生成表单
     * @return table info
     */
    public static CodegenTable toCodegenTable(TableInfo tableInfo, CodegenTableImportForm codegenTableImportForm) {
        val codegenTable = new CodegenTable();
        codegenTable.setDataSourceId(codegenTableImportForm.getDataSourceId());
        codegenTable.setTableName(tableInfo.getName());
        codegenTable.setTableComment(tableInfo.getComment());
        codegenTable.setClassName(tableInfo.getEntityName());
        codegenTable.setAuthor(codegenTableImportForm.getAuthor());
        codegenTable.setBasePackage(codegenTableImportForm.getBasePackage());
        codegenTable.setIgnoreTablePrefix(codegenTableImportForm.getIgnoreTablePrefix());
        codegenTable.setIgnoreColumnPrefix(codegenTableImportForm.getIgnoreColumnPrefix());
        return codegenTable;
    }

    /**
     * 构建代码生成列
     * @param id 代码生成表 id
     * @param fields table field list
     * @return codegen column list
     */
    public static List<CodegenColumn> toCodegenColumnList(Long id, List<TableField> fields) {
        val codegenColumns = new ArrayList<CodegenColumn>(fields.size());
        for (int i = 0; i < fields.size(); i++) {
            val tableField = fields.get(i);
            val codegenColumn = new CodegenColumn();
            codegenColumn.setTableId(id);
            codegenColumn.setColumnName(tableField.getColumnName());
            codegenColumn.setColumnComment(tableField.getComment());
            codegenColumn.setDbType(String.valueOf(tableField.getMetaInfo().getJdbcType()));
            codegenColumn.setNullable(tableField.getMetaInfo().isNullable());
            codegenColumn.setPrimaryKey(tableField.isKeyFlag());
            codegenColumn.setSortId(i);
            val javaType = tableField.getColumnType().getType();
            codegenColumn.setJavaType(javaType);
            // string类型记录长度
            if (DbColumnType.STRING.getType().equals(javaType)) {
                codegenColumn.setColumnLength(tableField.getMetaInfo().getLength());
            }
            codegenColumn.setJavaField(tableField.getPropertyName());
            // js类型
            codegenColumn.setJsType(getJsType(javaType));
            codegenColumn.setInsertField(isCreateOpsFiled(tableField));
            codegenColumn.setUpdateField(isUpdateOpsFiled(tableField));
            codegenColumn.setSelectField(isQueryField(tableField));
            codegenColumns.add(codegenColumn);
        }
        return codegenColumns;
    }

    /**
     * 是否插入操作字段
     * @param tableField table field
     * @return boolean
     */
    private static boolean isCreateOpsFiled(TableField tableField) {
        val isPrimaryKey = tableField.isKeyFlag();
        val javaField = tableField.getPropertyName();

        return !isPrimaryKey && !IGNORE_CREATE_UPDATE_FIELDS.contains(javaField);
    }

    /**
     * 是否更新操作字段
     * @param tableField table field
     * @return boolean
     */
    private static boolean isUpdateOpsFiled(TableField tableField) {
        val javaField = tableField.getPropertyName();
        return !IGNORE_CREATE_UPDATE_FIELDS.contains(javaField);
    }

    /**
     * 是否查询操作展示字段
     * @param tableField table field
     * @return boolean
     */
    private static boolean isQueryField(TableField tableField) {
        val javaField = tableField.getPropertyName();
        return !IGNORE_QUERY_FIELDS.contains(javaField);
    }

    /**
     * 验证表信息
     *
     * @param tableInfo table info
     */
    public static void validateTableInfo(TableInfo tableInfo) {
        if (tableInfo == null) {
            throw new CodegenFailedException("表不存在");
        }
        val tableName = tableInfo.getName();
        val tableComment = tableInfo.getComment();
        if (!StringUtils.hasText(tableComment)) {
            throw new CodegenFailedException("表没有备注: " + tableName);
        }
        val fields = tableInfo.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            throw new CodegenFailedException("字段不存在: " + tableName);
        }
        fields.forEach(field -> {
            val fieldName = field.getName();
            val fieldComment = field.getComment();
            if (!StringUtils.hasText(fieldComment)) {
                throw new CodegenFailedException("字段没有备注: " + tableName + "." + fieldName);
            }
        });
    }

    public static String getJsType(String javaType) {
        if (DbColumnType.BOOLEAN.getType().equals(javaType)) {
            return "boolean";
        }

        if (JS_NUMBER_TYPE.contains(javaType)) {
            return "number";
        }

        return "string";
    }
}
