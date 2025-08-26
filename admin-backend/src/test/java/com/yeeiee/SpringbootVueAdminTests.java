package com.yeeiee;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import com.yeeiee.service.DataSourceService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(args = {"--mpw.key=18mTlw4bqrAmvNXB"})
class SpringbootVueAdminTests {
    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames); // 可选：对Bean名称进行排序
        System.out.println("All beans in the ApplicationContext:");
        Arrays.stream(beanNames).forEach(System.out::println);
    }


    @Autowired
    private DataSourceService dataSourceService;

    @Test
    void codeGenTest() {
        val tableInfoList = getTableInfoList(1L, null);
        System.out.println("tableInfoList = " + tableInfoList);
    }

    private List<TableInfo> getTableInfoList(Long id, String name) {
        val dataSource = dataSourceService.getById(id);
        val url = dataSource.getUrl();
        val username = dataSource.getUsername();
        val password = dataSource.getPassword();

        // 使用 MyBatis Plus Generator 解析表结构
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(
                url,
                username,
                password
        );

        // 特殊：SQLServer jdbc 非标准，参见 https://github.com/baomidou/mybatis-plus/issues/5419
        val dbType = JdbcUtils.getDbType(url);
        if(DbType.SQL_SERVER == dbType || DbType.SQL_SERVER2005 == dbType){
            dataSourceConfigBuilder.databaseQueryClass(SQLQuery.class);

        }

        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder().enableSkipView(); // 忽略视图，业务上一般用不到
        if (StringUtils.hasText(name)) {
            strategyConfig.addInclude(name);
        }

        GlobalConfig globalConfig = new GlobalConfig.Builder().dateType(DateType.TIME_PACK).build(); // 只使用 LocalDateTime 类型，不使用 LocalDate
        ConfigBuilder builder = new ConfigBuilder(null, dataSourceConfigBuilder.build(), strategyConfig.build(),
                null, globalConfig, null);
        // 按照名字排序
        List<TableInfo> tables = builder.getTableInfoList();
        tables.sort(Comparator.comparing(TableInfo::getName));
        return tables;
    }
}
