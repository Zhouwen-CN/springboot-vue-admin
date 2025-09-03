package com.yeeiee.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
public class IdTypeEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String MPW_KEY = "mpw.key";
    private static final String NPW_PREFIX = "mpw:";
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
        // 解密key
        val mpwKey = environment.getProperty(MPW_KEY);
        var url = environment.getProperty(DATASOURCE_URL_KEY, String.class);
        Assert.notNull(url, "spring.datasource.url is null");
        if (url.startsWith(NPW_PREFIX) && StringUtils.hasText(mpwKey)) {
            url = AES.decrypt(url.replaceFirst(NPW_PREFIX, ""), mpwKey);
        }

        return JdbcUtils.getDbType(url);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
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

    private IdType getIdType(ConfigurableEnvironment environment) {
        return environment.getProperty(ID_TYPE_KEY, IdType.class);
    }

    public void setIdType(ConfigurableEnvironment environment, IdType idType) {
        environment.getSystemProperties().put(ID_TYPE_KEY, idType);
        log.warn("Set mybatis idType to [{}]", idType);
    }
}
