package com.yeeiee.controller;

import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户 控制器
 * </p>
 *
 * @author chen
 * @since 2024/5/6
 */
@RestController
@Tag(name = "用户表 控制器")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @Operation(summary = "获取用户信息")
    @GetMapping("/user")
    public R<User> getUser() {
        val serviceUser = userService.getUser();
        return R.ok(serviceUser);
    }

    @Operation(summary = "用户登入")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        val token = userService.login(loginDto);
        return R.ok(token);
    }
}
