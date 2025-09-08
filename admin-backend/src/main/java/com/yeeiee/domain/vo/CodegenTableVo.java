package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 代码生成表视图
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenTableVo", description = "代码生成表视图")
public class CodegenTableVo {
    @Schema(description = "表名称")
    private String name;

    @Schema(description = "表描述")
    private String comment;
}
