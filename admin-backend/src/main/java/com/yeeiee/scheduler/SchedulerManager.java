package com.yeeiee.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 调度器管理
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@RequiredArgsConstructor
@Service
public class SchedulerManager {
    public static final String ID = "id";
    public static final String SCRIPT = "script";
    private final Scheduler scheduler;

    /**
     * 添加任务
     *
     * @param jobId          任务id
     * @param jobName        任务名称
     * @param script         执行脚本
     * @param cronExpression cron表达式
     * @throws SchedulerException 调度异常
     */
    public void addJob(Long jobId, String jobName, String script, String cronExpression, boolean immediate) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(JobExecutor.class)
                .withIdentity(jobName)
                .usingJobData(ID, jobId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(SCRIPT, script)
                .build();

        scheduler.scheduleJob(jobDetail, Set.of(trigger), true);

        if (!immediate) {
            this.pauseJob(jobName);
        }
    }

    /**
     * 更新任务
     *
     * @param jobName        任务名称
     * @param script         执行脚本
     * @param cronExpression cron表达式
     * @throws SchedulerException 调度异常
     */
    public void updateJob(String jobName, String script, String cronExpression)
            throws SchedulerException {

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(SCRIPT, script)
                .build();

        scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), trigger);
    }

    /**
     * 删除任务
     *
     * @param jobName 任务名称
     * @throws SchedulerException 调度异常
     */
    public void deleteJob(String jobName) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
        scheduler.deleteJob(JobKey.jobKey(jobName));
    }

    /**
     * 暂停任务
     *
     * @param jobName 任务名称
     * @throws SchedulerException 调度异常
     */
    private void pauseJob(String jobName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName));
    }

    /**
     * 恢复任务
     *
     * @param jobName 任务名称
     * @throws SchedulerException 调度异常
     */
    private void resumeJob(String jobName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName));
    }

    /**
     * 启动/暂停 任务
     *
     * @param jobName 任务名称
     * @param enable  是否启动
     * @throws SchedulerException 调度异常
     */
    public void switchJob(String jobName, boolean enable) throws SchedulerException {
        if (enable) {
            this.resumeJob(jobName);
        } else {
            this.pauseJob(jobName);
        }
    }

    /**
     * 触发一次任务
     *
     * @param jobId   任务id
     * @param jobName 任务名称
     * @param script  执行脚本
     * @throws SchedulerException 调度异常
     */
    public void triggerJob(Long jobId, String jobName, String script)
            throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put(ID, jobId);
        data.put(SCRIPT, script);
        scheduler.triggerJob(JobKey.jobKey(jobName), data);
    }

    /**
     * 清空所有任务，慎用
     *
     * @throws SchedulerException 调度异常
     */
    public void clean() throws SchedulerException {
        scheduler.clear();
    }
}
