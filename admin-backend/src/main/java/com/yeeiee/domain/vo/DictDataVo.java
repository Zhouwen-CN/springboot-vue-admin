package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 字典数据视图对象
 * </p>
 *
 * @author chen
 * @since 2025-02-16
 */
@Getter
@Setter
@ToString
@Schema(name = "DictDataVo", description = "字典数据视图对象")
public class DictDataVo {
    @Schema(description = "标签建")
    private String label;
    @Schema(description = "标签值")
    private Integer value;
}
