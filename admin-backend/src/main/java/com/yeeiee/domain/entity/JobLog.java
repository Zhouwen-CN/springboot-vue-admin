package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yeeiee.enumeration.JobStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务日志表
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
@Getter
@Setter
@ToString
@TableName("t_job_log")
@KeySequence("t_job_log_seq")
public class JobLog {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 任务编号
     */
    @TableField(value = "job_id")
    private Long jobId;

    /**
     * 第几次执行
     */
    @TableField(value = "fire_num")
    private Integer fireNum;

    /**
     * 任务状态
     */
    @TableField(value = "status")
    private JobStatusEnum status;

    /**
     * js日志
     */
    @TableField(value = "js_log")
    private String jsLog;

    /**
     * 错误信息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}