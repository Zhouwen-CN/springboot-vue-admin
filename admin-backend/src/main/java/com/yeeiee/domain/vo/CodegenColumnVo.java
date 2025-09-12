package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 代码生成字段表视图
 * </p>
 *
 * @author chen
 * @since 2025-09-12
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenColumnVo", description = "代码生成字段表视图")
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

    @Schema(description = "是否允许为空")
    private Boolean nullable;

    @Schema(description = "是否为insert字段")
    private Boolean insertField;

    @Schema(description = "是否为update字段")
    private Boolean updateField;

    @Schema(description = "是否为select字段")
    private Boolean selectField;
}
