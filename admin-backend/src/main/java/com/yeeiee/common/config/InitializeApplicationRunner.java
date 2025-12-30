package com.yeeiee.common.config;

import com.yeeiee.codegen.domain.entity.DataSource;
import com.yeeiee.codegen.service.DataSourceService;
import com.yeeiee.scheduler.SchedulerManager;
import com.yeeiee.scheduler.domain.entity.Job;
import com.yeeiee.scheduler.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.quartz.SchedulerException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 初始化工作
 * </p>
 *
 * @author chen
 * @since 2025-08-25
 */
@RequiredArgsConstructor
@Configuration
public class InitializeApplicationRunner implements ApplicationRunner {

    private static final String CURRENT_DATASOURCE_NAME = "master";
    private final DataSourceService dataSourceService;
    private final DataSourceProperties dataSourceProperties;
    private final SchedulerManager schedulerManager;
    private final JobService jobService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initDataSourceConfig();
        this.initQuartzJob();
    }

    /**
     * 初始化当前数据源配置
     */
    private void initDataSourceConfig() {
        var dataSource = dataSourceService.lambdaQuery()
                .eq(DataSource::getName, CURRENT_DATASOURCE_NAME)
                .one();

        val notExists = dataSource == null;

        if (notExists) {
            dataSource = new DataSource();
            dataSource.setName(CURRENT_DATASOURCE_NAME);
        }

        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        if (notExists) {
            dataSourceService.save(dataSource);
        } else {
            dataSourceService.updateById(dataSource);
        }
    }

    /**
     * 初始化定时任务
     *
     * @throws SchedulerException 调度异常
     */
    private void initQuartzJob() throws SchedulerException {
        // schedulerManager.clear();

        val jobList = jobService.lambdaQuery()
                .list();

        for (Job job : jobList) {
            schedulerManager.addJob(
                    job.getId(),
                    job.getName(),
                    job.getHandlerName(),
                    job.getHandlerParam(),
                    job.getCronExpression(),
                    job.getRetryCount(),
                    job.getRetryInterval(),
                    job.getJobEnable()
            );
        }
    }
}
