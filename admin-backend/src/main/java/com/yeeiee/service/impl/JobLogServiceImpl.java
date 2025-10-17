package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.JobLog;
import com.yeeiee.domain.vo.JobLogVo;
import com.yeeiee.enumeration.JobStatusEnum;
import com.yeeiee.mapper.JobLogMapper;
import com.yeeiee.service.JobLogService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
@RequiredArgsConstructor
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {
    private final JobLogMapper jobLogMapper;

    @Override
    public void addJobLogWithResult(long jobId, String jobHandlerName, String jobHandlerParam, int fireNum, String result) {
        val jobLog = new JobLog();
        jobLog.setJobId(jobId);
        jobLog.setHandlerName(jobHandlerName);
        jobLog.setHandlerParam(jobHandlerParam);
        jobLog.setStatus(JobStatusEnum.FAILURE);
        jobLog.setFireNum(fireNum);
        jobLog.setResult(result);
        this.save(jobLog);
    }

    @Override
    public Long addJobLog(long jobId, String jobHandlerName, String jobHandlerParam, int fireNum) {
        val jobLog = new JobLog();
        jobLog.setJobId(jobId);
        jobLog.setHandlerName(jobHandlerName);
        jobLog.setHandlerParam(jobHandlerParam);
        jobLog.setStatus(JobStatusEnum.RUNNING);
        jobLog.setFireNum(fireNum);
        this.save(jobLog);
        return jobLog.getId();
    }

    @Override
    public void modifyJobLog(Long jobLogId, long time, Throwable exception, String result) {
        val jobLog = new JobLog();

        if (exception != null) {
            jobLog.setStatus(JobStatusEnum.FAILURE);
            jobLog.setResult(ExceptionUtils.getRootCauseMessage(exception));
        } else {
            jobLog.setStatus(JobStatusEnum.SUCCESS);
            jobLog.setResult(result);
        }

        jobLog.setId(jobLogId);
        jobLog.setTime(time);
        this.updateById(jobLog);
    }

    @Override
    public IPage<JobLogVo> getJobLogPage(Page<?> page, Long jobId, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        return jobLogMapper.selectJobLogPage(page, jobId, status, startTime, endTime);
    }
}