package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 字典类型视图
 * </p>
 *
 * @author chen
 * @since 2025-10-22
 */
@Getter
@Setter
@ToString
@Schema(name = "DictTypeVo", description = "字典类型视图")
public class DictTypeVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字典名称")
    private String name;

    @Schema(description = "字典是否启用：0-禁用，1-启用")
    private Boolean dictEnable;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}