package com.yeeiee.codegen.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 代码生成表配置 dto
 * </p>
 *
 * @author chen
 * @since 2025-09-11
 */
@Getter
@Setter
@ToString
public class CodegenTableConfigDto {
    private String includeTableName;
    private String ignoreTablePrefix;
    private String ignoreColumnPrefix;
}
