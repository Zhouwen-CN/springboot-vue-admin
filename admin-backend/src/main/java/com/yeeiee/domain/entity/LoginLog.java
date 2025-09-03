package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
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
 * @since 2025-06-27
 */
@Getter
@Setter
@ToString
@TableName("t_login_log")
@Schema(name = "LoginLog", description = "登录日志表")
public class LoginLog {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "用户名")
    @TableField(value = "username", keepGlobalFormat = true)
    private String username;

    @Schema(description = "操作类型")
    @TableField(value = "operation", keepGlobalFormat = true)
    private LoginOperationEnum operation;

    @Schema(description = "操作状态")
    @TableField(value = "status", keepGlobalFormat = true)
    private OperationStatusEnum status;

    @Schema(description = "ip地址")
    @TableField(value = "ip", keepGlobalFormat = true)
    private String ip;

    @Schema(description = "用户代理")
    @TableField(value = "user_agent", keepGlobalFormat = true)
    private String userAgent;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT, keepGlobalFormat = true)
    private LocalDateTime createTime;
}