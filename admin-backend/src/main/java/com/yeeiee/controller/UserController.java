package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.form.ChangePasswordForm;
import com.yeeiee.domain.form.UserForm;
import com.yeeiee.domain.vo.UserRoleVo;
import com.yeeiee.domain.vo.UserVo;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户表 控制器")
public class UserController {
    private final UserService userService;

    @Operation(summary = "查询用户分页")
    @GetMapping("/{size}/{current}")
    public R<IPage<UserRoleVo>> getUserPages(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                             @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                             @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName) {
        val list = userService.getUserPages(new Page<>(current, size), searchName);
        return R.ok(list);
    }

    @Operation(summary = "刷新token")
    @GetMapping("/refresh")
    public R<UserVo> refreshToken(HttpServletRequest request) {
        val userVo = userService.refreshToken(request);
        return R.ok(userVo);
    }

    @Operation(summary = "退出登入")
    @GetMapping("/logout/{id}")
    public R<Void> logout(@PathVariable("id") Long id) {
        userService.logout(id);
        return R.ok();
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<Void> addUser(@Validated(UserForm.Create.class) @RequestBody UserForm userForm) {
        userService.addUser(userForm);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping
    public R<Void> modifyUser(@Validated(UserForm.Update.class) @RequestBody UserForm userForm) {
        userService.modifyUser(userForm);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> removeUserById(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping
    public R<Void> removeUserByIds(@RequestParam("ids") @Parameter(description = "需要删除的用户id列表") Collection<Long> ids) {
        userService.removeUserByIds(ids);
        return R.ok();
    }

    @Operation(summary = "修改用户密码")
    @PatchMapping("/pwd/change")
    public R<Void> modifyUserChangePwd(@Validated @RequestBody ChangePasswordForm changePasswordForm) {
        userService.modifyUserChangePwd(changePasswordForm);
        return R.ok();
    }

    @Operation(summary = "重置用户密码")
    @PatchMapping("/pwd/reset")
    public R<Void> resetPasswordById(@RequestParam("id") Long id) {
        userService.modifyUserResetPwd(id);
        return R.ok();
    }
}

