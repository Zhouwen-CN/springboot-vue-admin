package com.yeeiee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

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
}
