package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.form.ChangePwdForm;
import com.yeeiee.domain.form.UserForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.domain.vo.UserVo;
import com.yeeiee.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户表 控制器")
public class UserController {
    private final UserService userService;

    @Operation(summary = "查询用户分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<UserVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName
    ) {
        val page = userService.getUserPages(Page.of(current, size), searchName);
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "退出登入")
    @GetMapping("/logout/{id}")
    public R<Void> logout(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.logout(id);
        return R.ok();
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody UserForm userForm) {
        userService.addUser(userForm);
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody UserForm userForm) {
        userService.modifyUser(userForm);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.removeUserById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "用户id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        userService.removeUserByIds(ids);
        return R.ok();
    }

    @Operation(summary = "修改用户密码")
    @PatchMapping("/pwd/change")
    public R<Void> changePwd(@Validated @RequestBody ChangePwdForm changePwdForm) {
        userService.modifyUserChangePwd(changePwdForm);
        return R.ok();
    }

    @Operation(summary = "重置用户密码")
    @PatchMapping("/pwd/reset/{id}")
    public R<Void> resetPwdById(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.modifyUserResetPwd(id);
        return R.ok();
    }
}

