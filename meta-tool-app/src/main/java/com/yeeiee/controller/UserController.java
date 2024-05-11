package com.yeeiee.controller;

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
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户表 控制器")
public class UserController extends BaseController<UserService, User> {

    public UserController(UserService service) {
        super(service);
    }

    @Operation(summary = "获取用户菜单")
    @GetMapping("/menus")
    public R<List<MenuDto>> getUser() {
        val userDto = service.getUserMenus();
        return R.ok(userDto);
    }

    @Operation(summary = "用户登入")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        val token = service.login(loginDto);
        return R.ok(token);
    }
    }

