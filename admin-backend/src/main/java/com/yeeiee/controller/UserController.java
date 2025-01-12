package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.TokenVo;
import com.yeeiee.entity.vo.UserRoleVo;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "刷新token")
    @GetMapping("/refresh")
    public R<TokenVo> refreshToken(HttpServletRequest request) {
        val tokenVo = userService.refreshToken(request);
        return R.ok(tokenVo);
    }

    @Operation(summary = "退出登入")
    @GetMapping("/logout/{id}")
    public R<String> logout(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.logout(id);
        return R.ok();
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
    public R<String> addUserWithRoleIds(@RequestBody UserRoleIdsDto userRoleIdsDto) {
        userService.addUser(userRoleIdsDto);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping("")
    public R<String> modifyUserWithRoleIds(@RequestBody UserRoleIdsDto userRoleIdsDto) {
        userService.modifyUser(userRoleIdsDto);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<String> removeUserById(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.removeUserById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("")
    public R<String> removeUserByIds(@RequestBody Collection<Long> ids) {
        userService.removeUserByIds(ids);
        return R.ok();
    }
}

