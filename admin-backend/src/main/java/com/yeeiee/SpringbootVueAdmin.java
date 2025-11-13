package com.yeeiee;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.yeeiee.codegen.domain.entity.CodegenColumn;
import com.yeeiee.codegen.domain.entity.CodegenTable;
import com.yeeiee.codegen.domain.form.CodegenTableImportForm;
import com.yeeiee.codegen.exception.CodegenFailedException;
import com.yeeiee.exception.IoRuntimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * springboot 启动类
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@EnableWebSecurity
@EnableCaching
@EnableTransactionManagement
@MapperScan("com.yeeiee.**.mapper")
// freemarker 只用来做代码生成，不需要自动装配
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class SpringbootVueAdmin {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootVueAdmin.class, args);
    }

    /**
     * <p>
     * 代码生成省工具类
     * </p>
     *
     * @author chen
     * @since 2025-09-10
     */
    public static final class CodegenUtil {

        // 新增修改忽略字段
        private static final Set<String> CREATE_UPDATE_IGNORE_FIELDS = Set.of("createTime", "updateTime", "createUser", "updateUser");

        // 查询条件字段映射
        private static final Map<String, CodegenColumnConditionEnum> SELECT_CONDITION_MAPPING = Map.of(
                "name", CodegenColumnConditionEnum.LIKE
        );
        // 查询忽略字段
        private static final Set<String> SELECT_IGNORE_FIELDS = Set.of("createUser", "updateUser");
        // 前端 number 类型映射
        private static final Set<String> JS_NUMBER_TYPE_MAPPING = Set.of(
                DbColumnType.BYTE.getType(),
                DbColumnType.SHORT.getType(),
                DbColumnType.INTEGER.getType(),
                DbColumnType.LONG.getType(),
                DbColumnType.FLOAT.getType(),
                DbColumnType.DOUBLE.getType()
        );
        // html 类型字段映射
        private static final Map<String, CodegenColumnHtmlTypeEnum> HTML_TYPE_MAPPING = Map.of(
                "time", CodegenColumnHtmlTypeEnum.DATETIME,
                "date", CodegenColumnHtmlTypeEnum.DATETIME
        );

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
                codegenColumn.setSelectResultField(isSelectResultField(tableField));
                codegenColumn.setInsertField(isInsertOpsFiled(tableField));
                codegenColumn.setUpdateField(isUpdateOpsFiled(tableField));
                codegenColumns.add(codegenColumn);
            }
            return codegenColumns;
        }

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
            codegenTable.setParentMenuId(codegenTableImportForm.getParentMenuId());
            codegenTable.setTableName(tableInfo.getName());
            codegenTable.setTableComment(tableInfo.getComment());
            val entityName = tableInfo.getEntityName();
            codegenTable.setClassName(entityName);
            codegenTable.setJavaBasePackage(codegenTableImportForm.getJavaBasePackage());
            codegenTable.setAuthor(codegenTableImportForm.getAuthor());
            codegenTable.setBusinessName(codegenTableImportForm.getBusinessName());
            codegenTable.setIgnoreTablePrefix(codegenTableImportForm.getIgnoreTablePrefix());
            codegenTable.setIgnoreColumnPrefix(codegenTableImportForm.getIgnoreColumnPrefix());
            return codegenTable;
        }

        /**
         * 获取html类型
         *
         * @param tableField table field
         * @return html type
         */
        private static String getHtmlType(TableField tableField) {
            val javaType = tableField.getColumnType().getType();
            // 如果是 Boolean 类型时，设置为 switch 类型.
            if (Boolean.class.getSimpleName().equals(javaType)) {
                return CodegenColumnHtmlTypeEnum.SWITCH.getType();
            }

            // 如果是 LocalDateTime 类型，则设置为 datetime 类型
            if (LocalDateTime.class.getSimpleName().equals(javaType)) {
                return CodegenColumnHtmlTypeEnum.DATETIME.getType();
            }

            // 按照字段名称给默认值（后缀）
            val entries = HTML_TYPE_MAPPING.entrySet();
            for (Map.Entry<String, CodegenColumnHtmlTypeEnum> entry : entries) {
                val key = entry.getKey();
                if (tableField.getName().toLowerCase().endsWith(key)) {
                    return entry.getValue().getType();
                }
            }

            return CodegenColumnHtmlTypeEnum.INPUT.getType();
        }

        /**
         * 获取查询条件
         *
         * @param tableField table field
         * @return select condition
         */
        private static String getSelectCondition(TableField tableField) {
            // 按照字段名称给默认值（后缀）
            val entries = SELECT_CONDITION_MAPPING.entrySet();
            for (Map.Entry<String, CodegenColumnConditionEnum> entry : entries) {
                val key = entry.getKey();
                if (tableField.getName().toLowerCase().endsWith(key)) {
                    return entry.getValue().getCondition();
                }
            }
            return CodegenColumnConditionEnum.EQ.getCondition();
        }

        /**
         * 是否查询操作结果字段
         *
         * @param tableField table field
         * @return boolean
         */
        private static boolean isSelectResultField(TableField tableField) {
            val javaField = tableField.getPropertyName();
            return !SELECT_IGNORE_FIELDS.contains(javaField);
        }

        /**
         * 是否插入操作字段
         *
         * @param tableField table field
         * @return boolean
         */
        private static boolean isInsertOpsFiled(TableField tableField) {
            val isPrimaryKey = tableField.isKeyFlag();
            val javaField = tableField.getPropertyName();

            return !isPrimaryKey && !CREATE_UPDATE_IGNORE_FIELDS.contains(javaField);
        }

        /**
         * 是否更新操作字段
         *
         * @param tableField table field
         * @return boolean
         */
        private static boolean isUpdateOpsFiled(TableField tableField) {
            val javaField = tableField.getPropertyName();
            return !CREATE_UPDATE_IGNORE_FIELDS.contains(javaField);
        }

        public static String getJsType(String javaType) {
            if (DbColumnType.BOOLEAN.getType().equals(javaType)) {
                return "boolean";
            }

            if (JS_NUMBER_TYPE_MAPPING.contains(javaType)) {
                return "number";
            }

            return "string";
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


        @Getter
        public enum CodegenColumnConditionEnum {
            EQ("="),
            GT(">"),
            GTE(">="),
            LT("<"),
            LTE("<="),
            LIKE("like");

            private final String condition;

            CodegenColumnConditionEnum(String condition) {
                this.condition = condition;
            }
        }

        @Getter
        public enum CodegenColumnHtmlTypeEnum {
            INPUT("input"), // 文本框
            TEXTAREA("textarea"), // 文本域
            SELECT("select"), // 下拉框
            SWITCH("switch"), // 单选框
            DATETIME("datetime"); // 日期控件

            private final String type;

            CodegenColumnHtmlTypeEnum(String type) {
                this.type = type;
            }
        }
    }

    /**
     * <p>
     * zip工具类
     * </p>
     *
     * @author chen
     * @since 2025-09-18
     */
    @Slf4j
    public static final class ZipUtil {
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        /**
         * zip压缩
         *
         * @param out   输出流
         * @param paths 文件路径列表
         * @param ins   输入流列表
         */
        public static void zip(OutputStream out, List<String> paths, List<? extends InputStream> ins) {
            if (CollectionUtils.isEmpty(paths) || CollectionUtils.isEmpty(ins)) {
                throw new IllegalArgumentException("Zip paths or ins is empty");
            }
            if (paths.size() != ins.size()) {
                throw new IllegalArgumentException("Zip paths and ins length not equal");
            }

            ZipOutputStream zip;
            if (out instanceof ZipOutputStream zipOutputStream) {
                zip = zipOutputStream;
            } else {
                zip = new ZipOutputStream(out, DEFAULT_CHARSET);
            }

            add(zip, paths, ins);

            flush(zip);
            close(zip);
        }

        /**
         * 遍历路径列表和数据流列表
         *
         * @param zip   ZipOutputStream
         * @param paths 文件路径列表
         * @param ins   输入流列表
         */
        private static void add(ZipOutputStream zip, List<String> paths, List<? extends InputStream> ins) {
            val size = paths.size();
            for (int i = 0; i < size; i++) {
                var path = paths.get(i);
                if (org.apache.commons.lang3.StringUtils.isBlank(path)) {
                    log.warn("Zip path is blank");
                    continue;
                }
                val in = ins.get(i);
                if (in == null) {
                    // 空目录需要检查路径规范性，目录以"/"结尾
                    path = org.apache.commons.lang3.StringUtils.appendIfMissing(path, "/");
                }

                putEntry(zip, path, in);
            }
        }

        /**
         * 将输入流，逐个添加到 ZipOutputStream 中
         *
         * @param zip  ZipOutputStream
         * @param path 文件路径
         * @param in   输入流
         */
        private static void putEntry(ZipOutputStream zip, String path, InputStream in) {
            try {
                zip.putNextEntry(new ZipEntry(path));
                if (in != null) {
                    in.transferTo(zip);
                }
                zip.closeEntry();
            } catch (IOException e) {
                throw new IoRuntimeException("Zip compress error: " + ExceptionUtils.getRootCauseMessage(e));
            } finally {
                close(in);
            }
        }

        /**
         * 关闭流
         *
         * @param closeable Closeable
         */
        private static void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    // do noting
                }
            }
        }

        /**
         * 刷写流
         *
         * @param flushable Flushable
         */
        private static void flush(Flushable flushable) {
            if (null != flushable) {
                try {
                    flushable.flush();
                } catch (Exception e) {
                    // do noting
                }
            }
        }
    }
}
