package com.yeeiee.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.exception.CodegenFailedException;
import lombok.Getter;
import lombok.val;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Getter
    public enum CodegenColumnHtmlTypeEnum {

        INPUT("input"), // 文本框
        TEXTAREA("textarea"), // 文本域
        SELECT("select"), // 下拉框
        RADIO("radio"), // 单选框
        CHECKBOX("checkbox"), // 复选框
        DATETIME("datetime"), // 日期控件
        ;

        private final String type;

        CodegenColumnHtmlTypeEnum(String type) {
            this.type = type;
        }
    }

    @Getter
    public enum CodegenColumnConditionEnum {
        EQ("="),
        GT(">"),
        GTE(">="),
        LT("<"),
        LTE("<="),
        LIKE("like"),
        BETWEEN("between");

        private final String condition;

        CodegenColumnConditionEnum(String condition) {
            this.condition = condition;
        }
    }


    private static final Set<String> IGNORE_CREATE_UPDATE_FIELDS = Set.of("createTime", "updateTime", "createUser", "updateUser");
    private static final Set<String> IGNORE_SELECT_FIELDS = Set.of("createUser", "updateUser");
    private static final Set<String> JS_NUMBER_TYPE = Set.of(
            DbColumnType.BYTE.getType(),
            DbColumnType.SHORT.getType(),
            DbColumnType.INTEGER.getType(),
            DbColumnType.LONG.getType(),
            DbColumnType.FLOAT.getType(),
            DbColumnType.DOUBLE.getType()
    );

    private static final Map<String, CodegenColumnConditionEnum> COLUMN_SELECT_CONDITION_MAPPING = Map.of(
            "name", CodegenColumnConditionEnum.LIKE,
            "time", CodegenColumnConditionEnum.BETWEEN,
            "date", CodegenColumnConditionEnum.BETWEEN
    );

    private static final Map<String, CodegenColumnHtmlTypeEnum> COLUMN_HTML_TYPE_MAPPING = Map.of(
            "status", CodegenColumnHtmlTypeEnum.RADIO,
            "sex", CodegenColumnHtmlTypeEnum.RADIO,
            "type", CodegenColumnHtmlTypeEnum.SELECT,
            "time", CodegenColumnHtmlTypeEnum.DATETIME,
            "date", CodegenColumnHtmlTypeEnum.DATETIME
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
        val entityName = tableInfo.getEntityName();
        codegenTable.setClassName(entityName);
        codegenTable.setAuthor(codegenTableImportForm.getAuthor());
        // 业务名称
        codegenTable.setBusinessName(getBusinessName(entityName));
        codegenTable.setIgnoreTablePrefix(codegenTableImportForm.getIgnoreTablePrefix());
        codegenTable.setIgnoreColumnPrefix(codegenTableImportForm.getIgnoreColumnPrefix());
        return codegenTable;
    }

    /**
     * 构建代码生成列
     *
     * @param id     代码生成表 id
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
            // html类型
            codegenColumn.setHtmlType(getHtmlType(tableField));
            // select条件
            codegenColumn.setSelectCondition(getSelectCondition(tableField));
            // 操作类型字段
            codegenColumn.setSelectConditionField(false);
            codegenColumn.setSelectResultField(isSelectField(tableField));
            codegenColumn.setInsertField(isCreateOpsFiled(tableField));
            codegenColumn.setUpdateField(isUpdateOpsFiled(tableField));
            codegenColumns.add(codegenColumn);
        }
        return codegenColumns;
    }

    /**
     * 获取业务名称，取第一个单词
     * @param entityName 实体名称
     * @return 业务名称
     */
    public static String getBusinessName(String entityName){
        val split = org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(entityName);
        if(split.length > 0){
            return org.apache.commons.lang3.StringUtils.uncapitalize(split[0]);
        }

        return org.apache.commons.lang3.StringUtils.uncapitalize(entityName);
    }

    /**
     * 获取html类型
     * @param tableField table field
     * @return html type
     */
    private static String getHtmlType(TableField tableField) {
        val javaType = tableField.getColumnType().getType();
        // 如果是 Boolean 类型时，设置为 radio 类型.
        if (Boolean.class.getSimpleName().equals(javaType)) {
            return CodegenColumnHtmlTypeEnum.RADIO.getType();
        }

        // 如果是 LocalDateTime 类型，则设置为 datetime 类型
        if (LocalDateTime.class.getSimpleName().equals(javaType)) {
            return CodegenColumnHtmlTypeEnum.DATETIME.getType();
        }

        // 按照字段名称给默认值（后缀）
        val entries = COLUMN_HTML_TYPE_MAPPING.entrySet();
        for (Map.Entry<String, CodegenColumnHtmlTypeEnum> entry : entries) {
            val key = entry.getKey();
            if (tableField.getName().endsWith(key)) {
                return entry.getValue().getType();
            }
        }

        return CodegenColumnHtmlTypeEnum.INPUT.getType();
    }


    /**
     * 获取查询条件
     * @param tableField table field
     * @return select condition
     */
    private static String getSelectCondition(TableField tableField) {
        // 按照字段名称给默认值（后缀）
        val entries = COLUMN_SELECT_CONDITION_MAPPING.entrySet();
        for (Map.Entry<String, CodegenColumnConditionEnum> entry : entries) {
            val key = entry.getKey();
            if (tableField.getName().endsWith(key)) {
                return entry.getValue().getCondition();
            }
        }
        return CodegenColumnConditionEnum.EQ.getCondition();
    }

    /**
     * 是否查询操作展示字段
     *
     * @param tableField table field
     * @return boolean
     */
    private static boolean isSelectField(TableField tableField) {
        val javaField = tableField.getPropertyName();
        return !IGNORE_SELECT_FIELDS.contains(javaField);
    }

    /**
     * 是否插入操作字段
     *
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
     *
     * @param tableField table field
     * @return boolean
     */
    private static boolean isUpdateOpsFiled(TableField tableField) {
        val javaField = tableField.getPropertyName();
        return !IGNORE_CREATE_UPDATE_FIELDS.contains(javaField);
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
