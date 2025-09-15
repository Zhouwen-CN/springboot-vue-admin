package com.yeeiee.domain.form;

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

    @NotNull
    @Schema(description = "是否允许为空")
    private Boolean nullable;

    @NotNull
    @Schema(description = "是否为insert字段")
    private Boolean insertField;

    @NotNull
    @Schema(description = "是否为update字段")
    private Boolean updateField;

    @NotNull
    @Schema(description = "是否为select字段")
    private Boolean selectField;
}
