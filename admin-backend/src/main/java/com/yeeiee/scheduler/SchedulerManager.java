package com.yeeiee.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
    public static final String HANDLER_NAME = "name";
    public static final String HANDLER_PARAM = "param";
    public static final String RETRY_COUNT = "retryCount";
    public static final String RETRY_INTERVAL = "retryInterval";

    private final Scheduler scheduler;

    /**
     * 添加任务
     *
     * @param jobId           任务id
     * @param jobName         任务名称
     * @param jobHandlerName  任务处理类名称
     * @param jobHandlerParam 任务处理参数
     * @param cronExpression  cron表达式
     * @param retryCount      重试次数
     * @param retryInterval   重试间隔
     * @param immediate       是否立即执行
     * @throws SchedulerException 调度异常
     */
    public void addJob(
            Long jobId,
            String jobName,
            String jobHandlerName,
            String jobHandlerParam,
            String cronExpression,
            Integer retryCount,
            Integer retryInterval,
            boolean immediate
    ) throws SchedulerException {

        val jobDetail = JobBuilder.newJob(JobHandlerInvoker.class)
                .withIdentity(jobName)
                .usingJobData(ID, jobId)
                .build();

        // 当前时间 - 开火时间 > misfireThreshold，则判定为失火；参考: CronTriggerImpl.updateAfterMisfire
        val cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                // 默认，失火后，下次开火时间 = 当前时间
                .withMisfireHandlingInstructionFireAndProceed();
        // 失火后，下次开火时间 = 当前时间 + 调度间隔
        // .withMisfireHandlingInstructionDoNothing()
        // 忽略失火。例如，每15秒触发一次，失火了5分钟，一旦有机会触发，就会触发 20 次
        // .withMisfireHandlingInstructionIgnoreMisfires();

        val trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName)
                .withSchedule(cronScheduleBuilder)
                .usingJobData(HANDLER_NAME, jobHandlerName)
                .usingJobData(HANDLER_PARAM, jobHandlerParam)
                .usingJobData(RETRY_COUNT, retryCount)
                .usingJobData(RETRY_INTERVAL, retryInterval)
                .build();

        scheduler.scheduleJob(jobDetail, Set.of(trigger), true);

        if (!immediate) {
            this.pauseJob(jobName);
        }
    }

    /**
     * 更新任务
     *
     * @param jobName         任务名称
     * @param jobHandlerName  任务处理类名称
     * @param jobHandlerParam 任务处理参数
     * @param cronExpression  cron表达式
     * @param retryCount      重试次数
     * @param retryInterval   重试间隔
     * @param immediate       是否立即执行
     * @throws SchedulerException 调度异常
     */
    public void updateJob(
            String jobName,
            String jobHandlerName,
            String jobHandlerParam,
            String cronExpression,
            Integer retryCount,
            Integer retryInterval,
            boolean immediate
    )
            throws SchedulerException {
        val trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .usingJobData(HANDLER_NAME, jobHandlerName)
                .usingJobData(HANDLER_PARAM, jobHandlerParam)
                .usingJobData(RETRY_COUNT, retryCount)
                .usingJobData(RETRY_INTERVAL, retryInterval)
                .build();

        scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), trigger);

        if (!immediate) {
            this.pauseJob(jobName);
        }
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
     * @param jobId         任务id
     * @param jobName       任务名称
     * @param jobHandlerName 任务处理类名称
     * @param jobHandlerParam 任务处理参数
     * @param retryCount    重试次数
     * @param retryInterval 重试间隔
     * @throws SchedulerException 调度异常
     */
    public void triggerJob(
            Long jobId,
            String jobName,
            String jobHandlerName,
            String jobHandlerParam,
            Integer retryCount,
            Integer retryInterval
    )
            throws SchedulerException {
        val jobDataMap = new JobDataMap();
        jobDataMap.put(ID, jobId);
        jobDataMap.put(HANDLER_NAME, jobHandlerName);
        jobDataMap.put(HANDLER_PARAM, jobHandlerParam);
        jobDataMap.put(RETRY_COUNT, retryCount);
        jobDataMap.put(RETRY_INTERVAL, retryInterval);
        scheduler.triggerJob(JobKey.jobKey(jobName), jobDataMap);
    }

    /**
     * 清空所有任务，慎用
     *
     * @throws SchedulerException 调度异常
     */
    public void clear() throws SchedulerException {
        scheduler.clear();
    }
}
