package com.yeeiee.scheduler.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.scheduler.domain.entity.JobLog;
import com.yeeiee.scheduler.domain.vo.JobLogVo;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务日志表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
public interface JobLogService extends IService<JobLog> {
    /**
     * 添加定时任务日志
     *
     * @param jobId           任务id
     * @param jobHandlerName  任务名称
     * @param jobHandlerParam 任务参数
     * @param fireNum         第几次执行
     * @param result          任务结果
     */
    void addJobLogWithResult(long jobId, String jobHandlerName, String jobHandlerParam, int fireNum, String result);

    /**
     * 添加定时任务日志
     *
     * @param jobId           任务id
     * @param jobHandlerName  任务名称
     * @param jobHandlerParam 任务参数
     * @param fireNum         第几次执行
     * @return 定时任务日志id
     */
    Long addJobLog(long jobId, String jobHandlerName, String jobHandlerParam, int fireNum);

    /**
     * 修改定时任务日志
     *
     * @param jobLogId  定时任务日志id
     * @param time      执行耗时
     * @param exception 异常
     * @param result    任务结果
     */
    void modifyJobLog(Long jobLogId, long time, Throwable exception, String result);

    /**
     * 获取定时任务日志分页
     *
     * @param page      分页参数
     * @param jobId     任务id
     * @param status    状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 定时任务日志分页
     */
    IPage<JobLogVo> getJobLogPage(Page<?> page, Long jobId, Integer status, LocalDateTime startTime, LocalDateTime endTime);
}