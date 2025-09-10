package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @since 2025-09-10
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

    @Schema(description = "字段类型")
    @TableField(value = "data_type")
    private String dataType;

    @Schema(description = "字段长度")
    @TableField(value = "column_length")
    private Integer columnLength;

    @Schema(description = "是否允许为空")
    @TableField(value = "nullable")
    private Boolean nullable;

    @Schema(description = "是否主键")
    @TableField(value = "primary_key")
    private Boolean primaryKey;

    @Schema(description = "排序")
    @TableField(value = "ordinal_position")
    private Integer ordinalPosition;

    @Schema(description = "java 属性类型")
    @TableField(value = "java_type")
    private String javaType;

    @Schema(description = "java 属性名")
    @TableField(value = "java_field")
    private String javaField;

    @Schema(description = "是否为insert字段")
    @TableField(value = "create_operation")
    private Boolean createOperation;

    @Schema(description = "是否为update字段")
    @TableField(value = "update_operation")
    private Boolean updateOperation;

    @Schema(description = "是否为list字段")
    @TableField(value = "list_operation")
    private Boolean listOperation;

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