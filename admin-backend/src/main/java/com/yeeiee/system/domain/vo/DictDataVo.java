package com.yeeiee.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 字典数据表视图
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
@Getter
@Setter
@ToString
@Schema(name = "DictDataVo", description = "字典数据表视图")
public class DictDataVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字典键")
    private String label;

    @Schema(description = "字典值")
    private Integer data;

    @Schema(description = "字典排序")
    private Integer sortId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}