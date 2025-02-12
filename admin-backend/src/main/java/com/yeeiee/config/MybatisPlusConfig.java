package com.yeeiee.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.val;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * mybatis plus插件
     * 总结：对 SQL 进行单次改造的插件应优先放入，不对 SQL 进行改造的插件最后放入。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        val interceptor = new MybatisPlusInterceptor();

        val paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 数据库类型，如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 方言实现类
        paginationInnerInterceptor.setDialect(new MySqlPaginationDialect());
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
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }

    /**
     * 更新时 字段填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }
}