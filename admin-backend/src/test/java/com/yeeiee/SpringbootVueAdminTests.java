package com.yeeiee;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
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
    DataSource dataSource;
    @Test
    void codeGenTest(){
        val tableInfoList = getTableInfoList(null);
        System.out.println("tableInfoList = " + tableInfoList);
    }

    private List<TableInfo> getTableInfoList(String name) {
        // 使用 MyBatis Plus Generator 解析表结构
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(dataSource);

        // if (JdbcUtils.isSQLServer(config.getUrl())) { // 特殊：SQLServer jdbc 非标准，参见 https://github.com/baomidou/mybatis-plus/issues/5419
        //     dataSourceConfigBuilder.databaseQueryClass(SQLQuery.class);
        // }

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
