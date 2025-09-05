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
    private static final String CURRENT_DATASOURCE_NAME = "master";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initDataSourceConfig();
    }

    /**
     * 初始化当前数据源配置
     */
    private void initDataSourceConfig(){
        var dataSource = dataSourceService.lambdaQuery()
                .eq(DataSource::getName, CURRENT_DATASOURCE_NAME)
                .one();

        val notExists = dataSource == null;

        if(notExists){
            dataSource = new DataSource();
            dataSource.setName(CURRENT_DATASOURCE_NAME);
        }

        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        if(notExists){
            dataSourceService.save(dataSource);
        }else{
            dataSourceService.updateById(dataSource);
        }
    }
}
