package com.yeeiee.scheduler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>
 * quartz线程池配置
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@Configuration
public class QuartzThreadPoolConfiguration {
    @Primary
    @Bean(name = "quartzTaskExecutionProperties")
    @ConfigurationProperties(prefix = "spring.task.execution.quartz")
    public TaskExecutionProperties quartzTaskExecutionProperties() {
        return new TaskExecutionProperties();
    }


    @Bean(name = "quartzThreadPoolTaskExecutorBuilder")
    public ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder(
            @Qualifier("quartzTaskExecutionProperties") TaskExecutionProperties properties
    ) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        ThreadPoolTaskExecutorBuilder builder = new ThreadPoolTaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        builder = builder.maxPoolSize(pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        return builder;
    }

    @Lazy
    @Bean(name = "quartzThreadPoolTaskExecutor")
    @ConditionalOnThreading(Threading.PLATFORM)
    ThreadPoolTaskExecutor applicationTaskExecutor(
            @Qualifier("quartzThreadPoolTaskExecutorBuilder") ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
    ) {
        return threadPoolTaskExecutorBuilder.build();
    }
}