package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 代码生成字段表
 * </p>
 *
 * @author chen
 * @since 2025-09-16
 */
@Getter
@Setter
@ToString
@TableName("t_codegen_column")
@KeySequence("t_codegen_column_seq")
@Schema(name = "CodegenColumn", description = "代码生成字段表")
public class CodegenColumn {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "代码生成表编号")
    @TableField(value = "table_id")
    private Long tableId;

    @Schema(description = "字段名")
    @TableField(value = "column_name")
    private String columnName;

    @Schema(description = "字段描述")
    @TableField(value = "column_comment")
    private String columnComment;

    @Schema(description = "数据库类型")
    @TableField(value = "db_type")
    private String dbType;

    @Schema(description = "字段长度")
    @TableField(value = "column_length")
    private Integer columnLength;

    @Schema(description = "java属性名")
    @TableField(value = "java_field")
    private String javaField;

    @Schema(description = "java类型")
    @TableField(value = "java_type")
    private String javaType;

    @Schema(description = "js类型")
    @TableField(value = "js_type")
    private String jsType;

    @Schema(description = "html类型")
    @TableField(value = "html_type")
    private String htmlType;

    @Schema(description = "排序")
    @TableField(value = "sort_id")
    private Integer sortId;

    @Schema(description = "字典类型编号")
    @TableField(value = "dict_type_id", updateStrategy = FieldStrategy.ALWAYS)
    private Long dictTypeId;

    @Schema(description = "select字段条件")
    @TableField(value = "select_condition")
    private String selectCondition;

    @Schema(description = "是否主键")
    @TableField(value = "primary_key")
    private Boolean primaryKey;

    @Schema(description = "是否允许为空")
    @TableField(value = "nullable")
    private Boolean nullable;

    @Schema(description = "是否为select条件字段")
    @TableField(value = "select_condition_field")
    private Boolean selectConditionField;

    @Schema(description = "是否select查询结果字段")
    @TableField(value = "select_result_field")
    private Boolean selectResultField;

    @Schema(description = "是否为insert字段")
    @TableField(value = "insert_field")
    private Boolean insertField;

    @Schema(description = "是否为update字段")
    @TableField(value = "update_field")
    private Boolean updateField;

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