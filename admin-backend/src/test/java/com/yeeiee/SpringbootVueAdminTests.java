package com.yeeiee;

import com.yeeiee.service.freemarker.FreemarkerEngineService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Map;

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
    FreemarkerEngineService freemarkerEngineService;

    @Test
    void freemarkerTest() {
        val map = freemarkerEngineService.codegenById(3L);
        val entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("entry.getKey() = \n" + entry.getKey());
            System.out.println("entry.getValue() = \n" + entry.getValue());
        }
    }
}
