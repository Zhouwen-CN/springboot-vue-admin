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
 * 代码生成表单
 * </p>
 *
 * @author chen
 * @since 2025-09-11
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenTableForm", description = "代码生成表单")
public class CodegenTableForm {
    @NotNull
    @Schema(description = "主键")
    private Long id;

    @NotNull
    @Schema(description = "上级菜单编号")
    private Long parentMenuId;

    @NotBlank
    @Length(max = 50)
    @Schema(description = "表名称")
    private String tableName;

    @NotBlank
    @Length(max = 100)
    @Schema(description = "表描述")
    private String tableComment;

    @NotBlank
    @Length(max = 100)
    @Schema(description = "类名称")
    private String className;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "作者")
    private String author;

    @NotBlank
    @Length(max = 50)
    @Schema(description = "业务名称")
    private String businessName;

    @Length(max = 20)
    @Schema(description = "忽略表前缀")
    private String ignoreTablePrefix;

    @Length(max = 20)
    @Schema(description = "忽略字段前缀")
    private String ignoreColumnPrefix;
}
