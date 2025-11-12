package com.yeeiee.codegen.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 代码生成表单
 * </p>
 *
 * @author chen
 * @since 2025-09-12
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenTableColumnsForm", description = "代码生成表单")
public class CodegenTableColumnsForm {
    @NotNull
    @Schema(description = "表信息")
    private CodegenTableForm table;

    // 字段列表最多100
    @NotNull
    @Size(min = 1, max = 100)
    @Schema(description = "字段列表")
    private List<CodegenColumnForm> columns;
}
