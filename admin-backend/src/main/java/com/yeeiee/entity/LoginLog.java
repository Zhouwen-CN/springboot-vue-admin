package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 登录日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@Getter
@Setter
@ToString
@TableName("`t_login_log`")
@Schema(name = "LoginLog", description = "登录日志表")
public class LoginLog {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("`username`")
    private String username;

    @Schema(description = "操作类型")
    @TableField("`operation`")
    private String operation;

    @Schema(description = "操作状态")
    @TableField("`status`")
    private String status;

    @Schema(description = "ip地址")
    @TableField("`ip`")
    private String ip;

    @Schema(description = "用户代理")
    @TableField("`user_agent`")
    private String userAgent;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
