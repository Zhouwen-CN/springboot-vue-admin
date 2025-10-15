package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.vo.JobLogVo;

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
     * @param jobId 任务id
     * @param fireNum 第几次执行
     * @return 定时任务日志id
     */
    Long addJobLog(long jobId, int fireNum);

    /**
     * 修改定时任务日志
     * @param jobLogId 定时任务日志id
     * @param exception 异常
     * @param jsLog js日志
     */
    void modifyJobLog(Long jobLogId, Throwable exception, String jsLog);

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