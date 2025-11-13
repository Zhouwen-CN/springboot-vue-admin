package com.yeeiee.system.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.enumeration.RequestMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
@Getter
@Setter
@ToString
@TableName("t_operation_log")
@KeySequence("t_operation_log_seq")
@Schema(name = "OperationLog", description = "操作日志表")
public class OperationLog {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "操作")
    @TableField(value = "operation")
    private String operation;

    @Schema(description = "请求地址")
    @TableField(value = "url")
    private String url;

    @Schema(description = "请求方式")
    @TableField(value = "method")
    private RequestMethodEnum method;

    @Schema(description = "请求参数")
    @TableField(value = "params")
    private String params;

    @Schema(description = "请求耗时")
    @TableField(value = "time")
    private Long time;

    @Schema(description = "操作状态")
    @TableField(value = "status")
    private OperationStatusEnum status;

    @Schema(description = "ip地址")
    @TableField(value = "ip")
    private String ip;

    @Schema(description = "用户代理")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(description = "创建者")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}