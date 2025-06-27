package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.yeeiee.enumeration.RequestMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 错误日志表
 * </p>
 *
 * @author chen
 * @since 2025-06-27
 */
@Getter
@Setter
@ToString
@TableName("t_error_log")
@Schema(name = "ErrorLog", description = "错误日志表")
public class ErrorLog {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField(value = "username", keepGlobalFormat = true)
    private String username;

    @Schema(description = "请求地址")
    @TableField(value = "url", keepGlobalFormat = true)
    private String url;

    @Schema(description = "请求方式")
    @TableField(value = "method", keepGlobalFormat = true)
    private RequestMethodEnum method;

    @Schema(description = "请求参数")
    @TableField(value = "params", keepGlobalFormat = true)
    private String params;

    @Schema(description = "ip地址")
    @TableField(value = "ip", keepGlobalFormat = true)
    private String ip;

    @Schema(description = "用户代理")
    @TableField(value = "user_agent", keepGlobalFormat = true)
    private String userAgent;

    @Schema(description = "错误信息")
    @TableField(value = "error_msg", keepGlobalFormat = true)
    private String errorMsg;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT, keepGlobalFormat = true)
    private LocalDateTime createTime;
}