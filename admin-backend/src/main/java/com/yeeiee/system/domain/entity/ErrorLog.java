package com.yeeiee.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yeeiee.common.enumeration.RequestMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 错误日志表
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
@Getter
@Setter
@ToString
@TableName("t_error_log")
@KeySequence("t_error_log_seq")
@Schema(name = "ErrorLog", description = "错误日志表")
public class ErrorLog {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "请求地址")
    @TableField(value = "url")
    private String url;

    @Schema(description = "请求方式")
    @TableField(value = "method")
    private RequestMethodEnum method;

    @Schema(description = "请求参数")
    @TableField(value = "params")
    private String params;

    @Schema(description = "ip地址")
    @TableField(value = "ip")
    private String ip;

    @Schema(description = "用户代理")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(description = "错误信息")
    @TableField(value = "error_msg")
    private String errorMsg;

    @Schema(description = "创建者")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}