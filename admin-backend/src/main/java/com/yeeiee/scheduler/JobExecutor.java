package com.yeeiee.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

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
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // TODO: 运行js脚本，待完善
        val name = context.getJobDetail().getKey().getName();
        val script = context.getMergedJobDataMap().getString("script");
        log.info("name={}\tscript={}", name, script);
    }
}
