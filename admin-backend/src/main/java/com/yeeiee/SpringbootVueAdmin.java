package com.yeeiee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@EnableWebSecurity
@EnableCaching
@EnableTransactionManagement
@MapperScan("com.yeeiee.mapper")
@SpringBootApplication
public class SpringbootVueAdmin {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootVueAdmin.class, args);
    }

}
