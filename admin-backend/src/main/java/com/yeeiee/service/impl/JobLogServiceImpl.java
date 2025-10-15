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
    public Long addJobLog(long jobId, int fireNum) {
        val jobLog = new JobLog();
        jobLog.setJobId(jobId);
        jobLog.setStatus(JobStatusEnum.RUNNING);
        jobLog.setFireNum(fireNum);
        this.save(jobLog);
        return jobLog.getId();
    }

    @Override
    public void modifyJobLog(Long jobLogId, Throwable exception, String jsLog) {
        val jobLog = new JobLog();
        var isSuccess = true;

        if (exception != null) {
            isSuccess = false;
            jobLog.setErrorMsg(ExceptionUtils.getRootCauseMessage(exception));
        }

        jobLog.setId(jobLogId);
        jobLog.setStatus(isSuccess ? JobStatusEnum.SUCCESS : JobStatusEnum.FAILURE);
        jobLog.setJsLog(jsLog);
        this.updateById(jobLog);
    }

    @Override
    public IPage<JobLogVo> getJobLogPage(Page<?> page, Long jobId, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        return jobLogMapper.selectJobLogPage(page, jobId, status, startTime, endTime);
    }
}