package com.yeeiee.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.impl.jdbcjobstore.PostgreSQLDelegate;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * <p>
 * 动态修改xml配置文件
 * <pre>
 *     1、根据数据源的 url 修改 mybatis idType 配置
 *     2、根据 DbType 修改 quartz 驱动代理配置
 * </pre>
 * </p>
 *
 * @author chen
 * @since 2025-09-03
 */
@Slf4j
public class EnvironmentConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";
    private static final String DATASOURCE_URL_KEY = "spring.datasource.master.url";
    private static final String QUARTZ_DRIVER_DELEGATE_CLASS_KEY = "spring.quartz.properties.org.quartz.jobStore.driverDelegateClass";
    private static final Set<DbType> INPUT_ID_TYPES = Set.of(
            DbType.H2,
            DbType.ORACLE, DbType.ORACLE_12C,
            DbType.POSTGRE_SQL,
            DbType.DB2,
            DbType.KINGBASE_ES,
            DbType.DM
    );

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        val environment = context.getEnvironment();

        // 根据url获取数据库类型
        val dbType = getDbType(environment);
        log.warn("Current dbType 【{}】", dbType);

        // 1、根据数据源的 url 修改 mybatis idType 配置
        val originalIdType = environment.getProperty(ID_TYPE_KEY, IdType.class);
        if (originalIdType == null || originalIdType == IdType.NONE) {
            var replaceIdType = IdType.AUTO;
            if (INPUT_ID_TYPES.contains(dbType)) {
                replaceIdType = IdType.INPUT;
            }
            this.setProperty(environment, ID_TYPE_KEY, replaceIdType, "Mybatis IdType");
        }

        // 2、根据 DbType 修改 quartz 驱动代理配置
        var delegateClass = environment.getProperty(QUARTZ_DRIVER_DELEGATE_CLASS_KEY, String.class);
        if (delegateClass == null) {
            delegateClass = getQuartDriverDelegateClass(dbType);
            this.setProperty(environment, QUARTZ_DRIVER_DELEGATE_CLASS_KEY, delegateClass, "Quartz DriverDelegateClass");
        }
    }

    private void setProperty(ConfigurableEnvironment environment, String key, Object value, String fieldName) {
        environment.getSystemProperties().put(key, value);
        log.warn("Set env 【{}】 to 【{}】", fieldName, value);
    }

    public static DbType getDbType(ConfigurableEnvironment environment) {
        val url = environment.getProperty(DATASOURCE_URL_KEY, String.class);
        Assert.notNull(url, DATASOURCE_URL_KEY + " is null");
        return JdbcUtils.getDbType(url);
    }

    private String getQuartDriverDelegateClass(DbType dbType) {
        switch (dbType) {
            case POSTGRE_SQL -> {
                return PostgreSQLDelegate.class.getName();
            }
            case ORACLE, ORACLE_12C -> {
                return OracleDelegate.class.getName();
            }
            default -> {
                return StdJDBCDelegate.class.getName();
            }
        }
    }
}