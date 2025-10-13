package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.Job;
import com.yeeiee.domain.form.JobEnableChangeForm;
import com.yeeiee.domain.form.JobForm;

/**
 * <p>
 * 定时任务表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
public interface JobService extends IService<Job> {
    /**
     * 添加job
     *
     * @param form job表单
     */
    void addJob(JobForm form);

    /**
     * 修改job
     *
     * @param form job表单
     */
    void modifyJob(JobForm form);

    /**
     * 删除job
     *
     * @param id   jobId
     * @param name 任务名称
     */
    void removeJob(Long id, String name);

    /**
     * 修改job状态
     *
     * @param id                  jobId
     * @param jobEnableChangeForm job状态修改表单
     */
    void modifyJobEnable(Long id, JobEnableChangeForm jobEnableChangeForm);

    /**
     * 触发一次任务
     *
     * @param id jobId
     */
    void triggerJob(Long id);
}