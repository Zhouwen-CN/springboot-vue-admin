package com.yeeiee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * springboot 启动类
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@EnableWebSecurity(debug = true)
@EnableCaching
@EnableTransactionManagement
@MapperScan("com.yeeiee.**.mapper")
// freemarker 只用来做代码生成，不需要自动装配
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class SpringbootVueAdmin {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootVueAdmin.class, args);
    }
}
