package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 字典类型表视图
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
@Getter
@Setter
@ToString
@Schema(name = "DictTypeVo", description = "字典类型表视图")
public class DictTypeVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典名称")
    private String name;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}