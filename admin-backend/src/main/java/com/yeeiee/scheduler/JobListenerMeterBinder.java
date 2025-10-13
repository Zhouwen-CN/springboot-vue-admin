package com.yeeiee.scheduler;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.val;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * quartz job监听器，暴露执行次数和耗时指标
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@Component
public class JobListenerMeterBinder implements JobListener, MeterBinder {
    private MeterRegistry registry;

    @Override
    public String getName() {
        return "quartz.job";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        Timer.Sample sample = Timer.start(registry);
        context.put(Timer.Sample.class, sample);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        // do noting
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        Timer.Sample sample = (Timer.Sample) context.get(Timer.Sample.class);
        sample.stop(Timer.builder(this.getName())
                .tags(this.getTags(context))
                .register(registry)
        );
    }

    private String[] getTags(JobExecutionContext context) {
        val key = context.getJobDetail().getKey();
        return new String[]{
                "group",
                key.getGroup(),
                "job",
                key.getName()
        };
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
    }
}
