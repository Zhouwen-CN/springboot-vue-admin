package com.yeeiee.scheduler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 定时任务选择器视图
 * </p>
 *
 * @author chen
 * @since 2025-10-17
 */
@Getter
@Setter
@ToString
@Schema(name = "JobSelectorVo", description = "定时任务选择器视图")
public class JobSelectorVo {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "任务名称")
    private String name;
}
