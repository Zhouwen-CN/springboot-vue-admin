package com.yeeiee.scheduler;

import com.yeeiee.service.JobLogService;
import com.yeeiee.service.js.JsEngineService;
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
public class JobExecutor extends QuartzJobBean {

    private final JsEngineService jsEngineService;
    private final JobLogService jobLogService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        val name = context.getJobDetail().getKey().getName();
        log.info("执行调度任务: {}",name);
        val mergedJobDataMap = context.getMergedJobDataMap();

        val jobId = mergedJobDataMap.getLong(SchedulerManager.ID);
        val script = mergedJobDataMap.getString(SchedulerManager.SCRIPT);
        val fireCount = context.getRefireCount();
        val retryCount = mergedJobDataMap.getInt(SchedulerManager.RETRY_COUNT);
        val retryInterval = mergedJobDataMap.getInt(SchedulerManager.RETRY_INTERVAL);

        Long jobLogId = null;
        Throwable exception = null;
        String jsLog = null;
        try {
            // 添加任务日志
            jobLogId = jobLogService.addJobLog(jobId, fireCount + 1);
            jsLog = jsEngineService.eval(script);
        } catch (Throwable e) {
            exception = e;
        }

        // 更新任务日志
        jobLogService.modifyJobLog(jobLogId, exception, jsLog);

        // 异常处理
        handlerException(exception, fireCount, retryCount, retryInterval);
    }

    private void handlerException(Throwable exception, int fireCount, int retryCount, int retryInterval) throws JobExecutionException {
        if (exception == null) {
            return;
        }
        // 如果达到重试上限，直接抛出异常
        if (fireCount >= retryCount) {
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
