package com.yeeiee;

import com.yeeiee.entity.Menu;
import com.yeeiee.entity.dto.UserDto;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MetaToolAppApplicationTests {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {

    }

    @Test
    void getMenusTest() {
        List<Menu> menuDtos = menuMapper.selectMenusByRoleIds(List.of(1L));
        for (Menu menu : menuDtos) {
            System.out.println(menu);
        }
    }

    @Test
    void getUserTest() {
        UserDto admin = userMapper.selectByUserName("admin");

        System.out.println(admin);
    }
}
