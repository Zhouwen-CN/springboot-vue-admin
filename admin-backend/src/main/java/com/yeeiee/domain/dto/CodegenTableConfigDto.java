package com.yeeiee.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
    private List<String> includeTableNames;
    private String ignoreTablePrefix;
    private String ignoreColumnPrefix;
}
