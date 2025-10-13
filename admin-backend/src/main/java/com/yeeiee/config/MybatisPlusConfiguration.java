package com.yeeiee.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.DB2KeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.DmKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.KingbaseKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.parser.cache.JdkSerialCaffeineJsqlParseCache;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yeeiee.utils.SecurityUserUtil;
import lombok.val;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDateTime;

/**
 * <p>
 * mybatis 配置类
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Configuration
public class MybatisPlusConfiguration implements MetaObjectHandler, InitializingBean {
    @Value("${custom.jsql-parser.cache.spec}")
    private String jSqlParserCacheSpec;
    private final DbType dbType;

    @Autowired
    public MybatisPlusConfiguration(ConfigurableEnvironment environment) {
        dbType = MybatisIdTypeConfigInitializer.getDbType(environment);
    }

    /**
     * mybatis plus插件
     * 总结：对 SQL 进行单次改造的插件应优先放入，不对 SQL 进行改造的插件最后放入。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        val interceptor = new MybatisPlusInterceptor();

        // 分页插件
        val paginationInnerInterceptor = new PaginationInnerInterceptor();
        // TODO: h2 默认是 postgresql 分页，这里h2用的oracle模式
        if (dbType == DbType.H2) {
            paginationInnerInterceptor.setDbType(DbType.ORACLE);
        } else {
            paginationInnerInterceptor.setDbType(dbType);
        }

        // 溢出总页数后是否进行处理
        paginationInnerInterceptor.setOverflow(true);
        // 单页分页条数限制，不会报错，会修正成 size=20
        paginationInnerInterceptor.setMaxLimit(20L);
        // 如果配置多个插件,切记分页最后添加
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 防全表更新与删除插件：插件默认拦截没有指定条件的 update 和 delete 语句
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 插入时 字段填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        val principal = SecurityUserUtil.getPrincipal();
        principal.ifPresent(username -> {
            this.strictInsertFill(metaObject, "createUser", String.class, username);
            this.strictInsertFill(metaObject, "updateUser", String.class, username);
        });
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }

    /**
     * 更新时 字段填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        val principal = SecurityUserUtil.getPrincipal();
        principal.ifPresent(username -> this.strictUpdateFill(metaObject, "updateUser", String.class, username));
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }


    /**
     * 当 idType 为 input 时，需要提供一个 IKeyGenerator
     *
     * @return IKeyGenerator
     */
    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plus.global-config.db-config", name = "id-type", havingValue = "INPUT")
    public IKeyGenerator keyGenerator() {
        if (dbType != null) {
            switch (dbType) {
                case DB2 -> {
                    return new DB2KeyGenerator();
                }
                case H2 -> {
                    return new H2KeyGenerator();
                }
                case KINGBASE_ES -> {
                    return new KingbaseKeyGenerator();
                }
                case ORACLE, ORACLE_12C -> {
                    return new OracleKeyGenerator();
                }
                case POSTGRE_SQL -> {
                    return new PostgreKeyGenerator();
                }
                case DM -> {
                    return new DmKeyGenerator();
                }
            }
        }
        throw new IllegalArgumentException(String.format("Cannot find a suitable IKeyGenerator implementation class for [%s]", dbType));
    }

    /**
     * jSqlParser 缓存，mybatis一些插件会使用 jSqlParser 解析 sql，比如分页
     *
     * @throws Exception ex
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        JsqlParserGlobal.setJsqlParseCache(new JdkSerialCaffeineJsqlParseCache(
                Caffeine.from(jSqlParserCacheSpec).build()
        ));
    }
}