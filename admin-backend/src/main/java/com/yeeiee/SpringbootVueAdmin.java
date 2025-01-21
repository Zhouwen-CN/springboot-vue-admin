package com.yeeiee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * springboot 启动类
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@MapperScan("com.yeeiee.mapper")
// @EnableWebSecurity(debug = true)
@SpringBootApplication
public class SpringbootVueAdmin {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootVueAdmin.class, args);
    }

}
