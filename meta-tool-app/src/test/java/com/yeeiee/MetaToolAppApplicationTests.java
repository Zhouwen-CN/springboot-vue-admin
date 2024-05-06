package com.yeeiee;

import com.yeeiee.security.JwtUserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MetaToolAppApplicationTests {

    @Autowired
    JwtUserDetailService jwtUserDetailService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        UserDetails admin = jwtUserDetailService.loadUserByUsername("test");

        System.out.println(admin);
        System.out.println(admin.getAuthorities());
    }
}
