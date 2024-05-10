package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.MenuDto;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户表 控制器")
public class UserController extends BaseController<User> {

    public UserController(IService<User> service) {
        super(service);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/menus")
    public R<List<MenuDto>> getUser() {
        val userDto = ((UserService) service).getUserMenus();
        return R.ok(userDto);
    }

    @Operation(summary = "用户登入")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        val token = ((UserService) service).login(loginDto);
        return R.ok(token);
    }
}

