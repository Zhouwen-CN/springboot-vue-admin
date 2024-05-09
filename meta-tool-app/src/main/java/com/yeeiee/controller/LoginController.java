package com.yeeiee.controller;

import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.UserDto;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登入
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
@AllArgsConstructor
@RestController
@Tag(name = "登入 控制器")
public class LoginController {

    private UserService userService;

    @Operation(summary = "获取用户信息")
    @GetMapping("/user")
    public R<UserDto> getUser() {
        // val userDto = userService.getUser();
        // return R.ok(userDto);
        return null;
    }

    @Operation(summary = "用户登入")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        // val token = userService.login(loginDto);
        // return R.ok(token);
        return null;
    }
}
