package com.yeeiee.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 字典类型选择器视图
 * </p>
 *
 * @author chen
 * @since 2025-09-16
 */
@Getter
@Setter
@ToString
@Schema(name = "DictTypeSelectorVo", description = "字典类型选择器视图")
public class DictTypeSelectorVo {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "字典名称")
    private String name;
}
