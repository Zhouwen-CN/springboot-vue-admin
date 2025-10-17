package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 代码生成字段视图
 * </p>
 *
 * @author chen
 * @since 2025-09-12
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenColumnVo", description = "代码生成字段视图")
public class CodegenColumnVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字段名")
    private String columnName;

    @Schema(description = "字段描述")
    private String columnComment;

    @Schema(description = "数据库类型")
    private String dbType;

    @Schema(description = "java属性名")
    private String javaField;

    @Schema(description = "java类型")
    private String javaType;

    @Schema(description = "js类型")
    private String jsType;

    @Schema(description = "html类型")
    private String htmlType;

    @Schema(description = "字典类型编号")
    private Long dictTypeId;

    @Schema(description = "select字段条件")
    private String selectCondition;

    @Schema(description = "是否为select条件字段")
    private Boolean selectConditionField;

    @Schema(description = "是否select查询结果字段")
    private Boolean selectResultField;

    @Schema(description = "是否为insert字段")
    private Boolean insertField;

    @Schema(description = "是否为update字段")
    private Boolean updateField;
}
