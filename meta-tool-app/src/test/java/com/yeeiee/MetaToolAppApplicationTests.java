package com.yeeiee;

import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.service.UserService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
class MetaToolAppApplicationTests {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        val userRoleIdsDto = new UserRoleIdsDto();

        userRoleIdsDto.setUsername("chenzhou");
        userRoleIdsDto.setPassword("chenzhou");
        userRoleIdsDto.setRoleIds(Collections.emptyList());
        userService.addUserWithRoleIds(userRoleIdsDto);
    }
}
