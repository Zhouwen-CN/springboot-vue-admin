package com.yeeiee;

import com.yeeiee.entity.dto.MenuDto;
import com.yeeiee.service.FirstLevelMenuService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetaToolAppApplicationTests {

    @Autowired
    FirstLevelMenuService firstLevelMenuService;

    @Test
    void contextLoads() {
        val menu = firstLevelMenuService.getMenus();
        for (MenuDto menuDto : menu) {
            System.out.println(menuDto);
        }
//        System.out.println(menu);
    }
}
