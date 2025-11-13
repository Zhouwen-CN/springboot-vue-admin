package com.yeeiee.scheduler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 定时任务状态修改表单
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@Getter
@Setter
@ToString
@Schema(name = "JobEnableChangeForm", description = "定时任务状态修改表单")
public class JobEnableChangeForm {

    @NotBlank
    @Length
    @Schema(description = "任务名称")
    private String name;

    @NotNull
    @Schema(description = "任务状态")
    private boolean jobEnable;
}
