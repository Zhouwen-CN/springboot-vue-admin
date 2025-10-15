package com.yeeiee.domain.vo;

import com.yeeiee.enumeration.JobStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务日志视图
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
@Getter
@Setter
@ToString
@Schema(name = "JobLogVo", description = "定时任务日志视图")
public class JobLogVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "第几次执行")
    private Integer fireNum;

    @Schema(description = "任务状态")
    private JobStatusEnum status;

    @Schema(description = "js日志")
    private String jsLog;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

}