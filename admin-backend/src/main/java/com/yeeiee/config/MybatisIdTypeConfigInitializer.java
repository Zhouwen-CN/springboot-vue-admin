package com.yeeiee.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * <p>
 * 根据数据源的url修改mybatis idType配置
 * </p>
 *
 * @author chen
 * @since 2025-09-03
 */
@Slf4j
public class MybatisIdTypeConfigInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";
    private static final String DATASOURCE_URL_KEY = "spring.datasource.url";

    private static final Set<DbType> INPUT_ID_TYPES = Set.of(
            DbType.DB2,
            DbType.H2,
            DbType.KINGBASE_ES,
            DbType.ORACLE, DbType.ORACLE_12C,
            DbType.POSTGRE_SQL,
            DbType.DM
    );

    public static DbType getDbType(ConfigurableEnvironment environment) {
        var url = environment.getProperty(DATASOURCE_URL_KEY, String.class);
        Assert.notNull(url, DATASOURCE_URL_KEY + " is null");
        return JdbcUtils.getDbType(url);
    }


    private IdType getIdType(ConfigurableEnvironment environment) {
        return environment.getProperty(ID_TYPE_KEY, IdType.class);
    }

    public void setIdType(ConfigurableEnvironment environment, IdType idType) {
        environment.getSystemProperties().put(ID_TYPE_KEY, idType);
        log.warn("Set mybatis idType to [{}]", idType);
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        val environment = context.getEnvironment();
        // 如果非none，则不处理
        IdType idType = this.getIdType(environment);
        if (idType != IdType.NONE) {
            return;
        }

        DbType dbType = getDbType(environment);

        if (INPUT_ID_TYPES.contains(dbType)) {
            this.setIdType(environment, IdType.INPUT);
            return;
        }

        this.setIdType(environment, IdType.AUTO);
    }
}