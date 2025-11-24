package com.yeeiee.scheduler.handler;

import com.yeeiee.common.exception.JobHandlerParamException;
import com.yeeiee.scheduler.SchedulerManager;
import com.yeeiee.scheduler.service.JobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 任务执行器
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@Slf4j
@RequiredArgsConstructor
public class JobHandlerInvoker extends QuartzJobBean {

    private final JobLogService jobLogService;
    private final JobHandlerHolder jobHandlerHolder;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        val beginTime = System.currentTimeMillis();
        val mergedJobDataMap = context.getMergedJobDataMap();
        val jobId = mergedJobDataMap.getLong(SchedulerManager.ID);
        val handlerName = mergedJobDataMap.getString(SchedulerManager.HANDLER_NAME);
        val fireCount = context.getRefireCount();
        val handlerParam = mergedJobDataMap.getString(SchedulerManager.HANDLER_PARAM);
        val jobHandler = jobHandlerHolder.getJobHandler(handlerName);

        // 如果jobHandler为空，记录日志并退出
        if (jobHandler == null) {
            jobLogService.addJobLogWithResult(
                    jobId,
                    handlerName,
                    handlerParam,
                    fireCount + 1,
                    "未找到对应的任务处理器"
            );
            return;
        }

        val retryCount = mergedJobDataMap.getInt(SchedulerManager.RETRY_COUNT);
        val retryInterval = mergedJobDataMap.getInt(SchedulerManager.RETRY_INTERVAL);

        Long jobLogId = null;
        Throwable exception = null;
        String result = null;
        try {
            // 添加任务日志
            jobLogId = jobLogService.addJobLog(jobId, handlerName, handlerParam, fireCount + 1);
            result = jobHandler.execute(handlerParam);
        } catch (Throwable e) {
            exception = e;
        }

        // 更新任务日志
        val time = System.currentTimeMillis() - beginTime;
        jobLogService.modifyJobLog(jobLogId, time, exception, result);

        // 异常处理
        handlerException(exception, fireCount, retryCount, retryInterval);
    }

    private void handlerException(Throwable exception, int fireCount, int retryCount, int retryInterval) throws JobExecutionException {
        if (exception == null) {
            return;
        }
        // 如果达到重试上限 或者是 任务参数异常，则无需重试
        if (fireCount >= retryCount || exception instanceof JobHandlerParamException) {
            throw new JobExecutionException(exception);
        }

        // 间隔一段时间重试
        try {
            TimeUnit.MILLISECONDS.sleep(retryInterval);
        } catch (InterruptedException e) {
            // do noting
        }

        // 第二个参数表示立即重试
        throw new JobExecutionException(exception, true);
    }
}
