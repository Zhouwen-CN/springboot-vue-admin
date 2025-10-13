package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.Job;
import com.yeeiee.domain.form.JobEnableChangeForm;
import com.yeeiee.domain.form.JobForm;
import com.yeeiee.exception.JobSchedulerException;
import com.yeeiee.mapper.JobMapper;
import com.yeeiee.scheduler.SchedulerManager;
import com.yeeiee.service.JobService;
import com.yeeiee.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@RequiredArgsConstructor
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    private final SchedulerManager schedulerManager;

    @Override
    public void addJob(JobForm form) {
        try {
            schedulerManager.addJob(
                    form.getId(),
                    form.getName(),
                    form.getJsScript(),
                    form.getCronExpression(),
                    false
            );
        } catch (SchedulerException e) {
            throw new JobSchedulerException("添加定时任务异常", e);
        }

        val entity = BeanUtil.toBean(form, Job.class);
        this.save(entity);
    }

    @Override
    public void modifyJob(JobForm form) {
        val job = this.lambdaQuery()
                .select(Job::getName, Job::getJobEnable)
                .eq(Job::getId, form.getId())
                .one();
        val oldJobName = job.getName();
        val newJobName = form.getName();

        try {
            // 如果任务名称发生改变，删除旧任务并新增
            if (!oldJobName.equals(newJobName)) {
                schedulerManager.deleteJob(oldJobName);
                schedulerManager.addJob(
                        form.getId(),
                        newJobName,
                        form.getJsScript(),
                        form.getCronExpression(),
                        job.getJobEnable()
                );
                // 否则更新任务
            } else {
                schedulerManager.updateJob(
                        oldJobName,
                        form.getJsScript(),
                        form.getCronExpression()
                );
            }

        } catch (SchedulerException e) {
            throw new JobSchedulerException("修改定时任务异常", e);
        }

        val entity = BeanUtil.toBean(form, Job.class);
        this.updateById(entity);
    }

    @Override
    public void removeJob(Long id, String name) {
        try {
            schedulerManager.deleteJob(name);
        } catch (SchedulerException e) {
            throw new JobSchedulerException("删除定时任务异常", e);
        }

        this.removeById(id);
    }

    @Override
    public void modifyJobEnable(Long id, JobEnableChangeForm jobEnableChangeForm) {
        try {
            schedulerManager.switchJob(jobEnableChangeForm.getName(), jobEnableChangeForm.isJobEnable());
        } catch (SchedulerException e) {
            throw new JobSchedulerException("启停定时任务异常", e);
        }

        this.lambdaUpdate()
                .eq(Job::getId, id)
                .set(Job::getJobEnable, jobEnableChangeForm.isJobEnable())
                .update();
    }

    @Override
    public void triggerJob(Long id) {
        val job = this.lambdaQuery()
                .select(Job::getName, Job::getJsScript)
                .eq(Job::getId, id)
                .one();

        try {
            schedulerManager.triggerJob(
                    id,
                    job.getName(),
                    job.getJsScript()
            );
        } catch (SchedulerException e) {
            throw new JobSchedulerException("触发定时任务异常", e);
        }
    }
}