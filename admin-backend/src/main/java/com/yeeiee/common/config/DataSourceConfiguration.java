package com.yeeiee.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * 数据源配置
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@Configuration
public class DataSourceConfiguration {
    /**
     * 主数据源配置
     */
    @Primary
    @Bean(name = "masterDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 主数据源
     */
    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public HikariDataSource masterDataSource(@Qualifier("masterDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    /**
     * quartz数据源配置
     */
    @Bean(name = "quartzDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * quartz数据源
     */
    @Bean(name = "quartzDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.quartz.hikari")
    @QuartzDataSource // 指定quartz数据源，参考：JdbcStoreTypeConfiguration.dataSourceCustomizer
    public HikariDataSource quartzDataSource(@Qualifier("quartzDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
