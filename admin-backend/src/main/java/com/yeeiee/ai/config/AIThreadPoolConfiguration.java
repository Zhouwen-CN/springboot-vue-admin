package com.yeeiee.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

/**
 * <pre>
 * ai 请求异步线程池
 * 参考: {@link WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#configureAsyncSupport(AsyncSupportConfigurer)}
 * </pre>
 *
 * @author chen
 * @since 2025-10-11
 */
@Configuration
public class AIThreadPoolConfiguration {
    @Bean(name = "aiTaskExecutionProperties")
    @ConfigurationProperties(prefix = "spring.task.execution.ai")
    public TaskExecutionProperties aiTaskExecutionProperties() {
        return new TaskExecutionProperties();
    }


    @Bean(name = "aiThreadPoolTaskExecutorBuilder")
    public ThreadPoolTaskExecutorBuilder aiThreadPoolTaskExecutorBuilder(
            @Qualifier("aiTaskExecutionProperties") TaskExecutionProperties properties
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
    @Bean(name = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    @ConditionalOnThreading(Threading.PLATFORM)
    public ThreadPoolTaskExecutor applicationTaskExecutor(
            @Qualifier("aiThreadPoolTaskExecutorBuilder") ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
    ) {
        return threadPoolTaskExecutorBuilder.build();
    }
}