package com.yeeiee.scheduler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务视图
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Getter
@Setter
@ToString
@Schema(name = "JobVo", description = "定时任务视图")
public class JobVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "处理器名称")
    private String handlerName;

    @Schema(description = "处理器参数")
    private String handlerParam;

    @Schema(description = "cron 表达式")
    private String cronExpression;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "重试间隔")
    private Integer retryInterval;

    @Schema(description = "是否启用")
    private Boolean jobEnable;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    // 非查询字段
    @Schema(description = "修改状态按钮loading")
    private Boolean loading = false;
}