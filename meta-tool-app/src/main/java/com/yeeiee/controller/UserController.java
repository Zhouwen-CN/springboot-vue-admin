package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.UserRoleMenuVo;
import com.yeeiee.entity.vo.UserRoleVo;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户表 控制器")
public class UserController {
    private UserService userService;

    @Operation(summary = "获取用户信息")
    @GetMapping("")
    public R<UserRoleMenuVo> getUserInfo() {
        val userDto = userService.getUserInfo();
        return R.ok(userDto);
    }

    @Operation(summary = "用户登入")
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        val token = userService.login(loginDto);
        return R.ok(token);
    }

    @Operation(summary = "查询所有用户")
    @GetMapping("/{size}/{current}")
    public R<IPage<UserRoleVo>> getUserPages(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                             @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                             @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName) {

        val list = userService.getUserPages(new Page<>(current, size), searchName);
        return R.ok(list);
    }

    @Operation(summary = "新增用户")
    @PostMapping("")
    public R<Void> addUserWithRoleIds(@RequestBody UserRoleIdsDto userRoleIdsDto) {
        userService.addUserWithRoleIds(userRoleIdsDto);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("")
    public R<Void> updateUserWithRoleIds(@RequestBody UserRoleIdsDto userRoleIdsDto) {
        userService.updateUserWithRoleIds(userRoleIdsDto);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> removeUser(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.removeUser(id);
        return R.ok();
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("")
    public R<Void> removeUsersWithRoleIds(@RequestBody Collection<Long> ids) {
        userService.removeUsers(ids);
        return R.ok();
    }
}

