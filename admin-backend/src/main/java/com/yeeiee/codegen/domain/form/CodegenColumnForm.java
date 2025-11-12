package com.yeeiee.codegen.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 代码生成字段表表单
 * </p>
 *
 * @author chen
 * @since 2025-09-11
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenColumnForm", description = "代码生成字段表表单")
public class CodegenColumnForm {
    @NotNull
    @Schema(description = "主键")
    private Long id;

    @NotBlank
    @Length(max = 100)
    @Schema(description = "字段描述")
    private String columnComment;

    @NotBlank
    @Length(max = 50)
    @Schema(description = "java属性名")
    private String javaField;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "java类型")
    private String javaType;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "js类型")
    private String jsType;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "html类型")
    private String htmlType;

    @NotNull
    @Schema(description = "字典类型编号")
    private Long dictTypeId;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "select字段条件")
    private String selectCondition;

    @NotNull
    @Schema(description = "是否为select条件字段")
    private Boolean selectConditionField;

    @NotNull
    @Schema(description = "是否select查询结果字段")
    private Boolean selectResultField;

    @NotNull
    @Schema(description = "是否为insert字段")
    private Boolean insertField;

    @NotNull
    @Schema(description = "是否为update字段")
    private Boolean updateField;
}
