package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2025-01-07
 */
@Getter
@Setter
@ToString
@TableName("`t_operation_log`")
@Schema(name = "OperationLog", description = "操作日志表")
public class OperationLog {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("`username`")
    private String username;

    @Schema(description = "操作")
    @TableField("`operation`")
    private String operation;

    @Schema(description = "请求地址")
    @TableField("`url`")
    private String url;

    @Schema(description = "请求方式")
    @TableField("`method`")
    private String method;

    @Schema(description = "请求参数")
    @TableField("`params`")
    private String params;

    @Schema(description = "请求耗时")
    @TableField("`time`")
    private Long time;

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
