package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 数据源配置选择器视图
 * </p>
 *
 * @author chen
 * @since 2025-09-10
 */
@Getter
@Setter
@ToString
@Schema(name = "DataSourceSelectorVo", description = "数据源配置选择器视图")
public class DataSourceSelectorVo {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "数据源名称")
    private String name;
}
