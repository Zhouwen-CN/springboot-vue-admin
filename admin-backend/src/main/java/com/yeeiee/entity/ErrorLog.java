package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @since 2025-01-07
 */
@Getter
@Setter
@ToString
@TableName("`t_error_log`")
@Schema(name = "ErrorLog", description = "错误日志表")
public class ErrorLog {

    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("`username`")
    private String username;

    @Schema(description = "请求地址")
    @TableField("`url`")
    private String url;

    @Schema(description = "请求方式")
    @TableField("`method`")
    private String method;

    @Schema(description = "请求参数")
    @TableField("`params`")
    private String params;

    @Schema(description = "ip地址")
    @TableField("`ip`")
    private String ip;

    @Schema(description = "用户代理")
    @TableField("`user_agent`")
    private String userAgent;

    @Schema(description = "错误信息")
    @TableField("`error_msg`")
    private String errorMsg;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
