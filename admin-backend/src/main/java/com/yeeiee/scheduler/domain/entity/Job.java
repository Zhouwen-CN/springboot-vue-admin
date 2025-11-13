package com.yeeiee.scheduler.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Getter
@Setter
@ToString
@TableName("t_job")
@KeySequence("t_job_seq")
public class Job {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 任务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 处理器名称
     */
    @TableField(value = "handler_name")
    private String handlerName;

    /**
     * 处理器参数
     */
    @TableField(value = "handler_param")
    private String handlerParam;

    /**
     * cron 表达式
     */
    @TableField(value = "cron_expression")
    private String cronExpression;

    /**
     * 重试次数
     */
    @TableField(value = "retry_count")
    private Integer retryCount;

    /**
     * 重试间隔
     */
    @TableField(value = "retry_interval")
    private Integer retryInterval;

    /**
     * 是否启用
     */
    @TableField(value = "job_enable")
    private Boolean jobEnable;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}