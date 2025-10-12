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
 * 代码生成导入表单
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenTableImportForm", description = "代码生成导入表单")
public class CodegenTableImportForm {
    @NotNull
    @Schema(description = "数据源配置的编号")
    private Long dataSourceId;

    @NotNull
    @Schema(description = "上级菜单编号")
    private Long parentMenuId;

    @NotBlank
    @Length(max = 20)
    @Schema(description = "作者")
    private String author;

    @Length(max = 20)
    @Schema(description = "忽略表前缀")
    private String ignoreTablePrefix;

    @Length(max = 20)
    @Schema(description = "忽略字段前缀")
    private String ignoreColumnPrefix;

    @NotNull
    @Schema(description = "表名")
    private String tableName;

    @NotNull
    @Schema(description = "业务名称")
    private String businessName;
}
