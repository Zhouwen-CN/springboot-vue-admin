package com.yeeiee.domain.form;

import com.yeeiee.domain.validate.CronExpression;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 定时任务表单
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Getter
@Setter
@ToString
@Schema(name = "JobForm", description = "定时任务表单")
public class JobForm {

    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "主键")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 32)
    @Schema(description = "任务名称")
    private String name;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @CronExpression(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "cron 表达式")
    private String cronExpression;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 32)
    @Schema(description = "处理器名称")
    private String handlerName;

    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 500)
    @Schema(description = "处理器参数")
    private String handlerParam;

    @Min(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, value = 0)
    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "重试次数")
    private Integer retryCount;

    @Min(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, value = 0)
    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "重试间隔")
    private Integer retryInterval;
}