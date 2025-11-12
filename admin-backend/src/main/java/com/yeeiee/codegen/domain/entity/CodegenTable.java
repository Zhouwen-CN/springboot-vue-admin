package com.yeeiee.codegen.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 代码生成表
 * </p>
 *
 * @author chen
 * @since 2025-09-22
 */
@Getter
@Setter
@ToString
@TableName("t_codegen_table")
@KeySequence("t_codegen_table_seq")
@Schema(name = "CodegenTable", description = "代码生成表")
public class CodegenTable {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "数据源配置的编号")
    @TableField(value = "data_source_id")
    private Long dataSourceId;

    @Schema(description = "上级菜单编号")
    @TableField(value = "parent_menu_id")
    private Long parentMenuId;

    @Schema(description = "表名称")
    @TableField(value = "table_name")
    private String tableName;

    @Schema(description = "表描述")
    @TableField(value = "table_comment")
    private String tableComment;

    @Schema(description = "类名称")
    @TableField(value = "class_name")
    private String className;

    @Schema(description = "java基础包名")
    @TableField(value = "java_base_package")
    private String javaBasePackage;

    @Schema(description = "作者")
    @TableField(value = "author")
    private String author;

    @Schema(description = "业务名称")
    @TableField(value = "business_name")
    private String businessName;

    @Schema(description = "忽略表前缀")
    @TableField(value = "ignore_table_prefix")
    private String ignoreTablePrefix;

    @Schema(description = "忽略字段前缀")
    @TableField(value = "ignore_column_prefix")
    private String ignoreColumnPrefix;

    @Schema(description = "创建者")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}