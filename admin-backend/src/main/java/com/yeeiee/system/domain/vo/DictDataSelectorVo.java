package com.yeeiee.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 字典数据选择器视图
 * </p>
 *
 * @author chen
 * @since 2025-02-16
 */
@Getter
@Setter
@ToString
@Schema(name = "DictDataSelectorVo", description = "字典数据选择器视图")
public class DictDataSelectorVo {
    @Schema(description = "标签建")
    private String label;
    @Schema(description = "标签值")
    private Integer data;
}
