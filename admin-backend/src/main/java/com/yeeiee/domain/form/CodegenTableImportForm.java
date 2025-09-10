package com.yeeiee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.List;

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

    @Length(min = 1, max = 20)
    @Schema(description = "作者")
    private String author;

    @Schema(description = "忽略表前缀")
    private String ignoreTablePrefix;

    @Schema(description = "忽略字段前缀")
    private String ignoreColumnPrefix;

    @NotBlank
    @Schema(description = "基础包名")
    private String basePackage;

    @Size(min = 1)
    @Schema(description = "表名")
    private List<String> tableNames;
}
