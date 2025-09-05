package com.yeeiee.config;

import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.val;
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

    private final DataSourceService dataSourceService;
    private final DataSourceProperties dataSourceProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initDataSourceConfig();
    }

    /**
     * 初始化当前数据源配置
     */
    private void initDataSourceConfig(){
        val dataSource = new DataSource();
        dataSource.setName("master");
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSourceService.saveOrUpdate(dataSource);
    }
}
