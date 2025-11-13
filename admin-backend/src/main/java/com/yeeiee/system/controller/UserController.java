package com.yeeiee.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.system.domain.entity.User;
import com.yeeiee.system.domain.form.ChangePwdForm;
import com.yeeiee.system.domain.form.UserForm;
import com.yeeiee.system.domain.validate.GroupingValidate;
import com.yeeiee.system.domain.vo.PageVo;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.domain.vo.UserVo;
import com.yeeiee.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
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

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<UserVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName
    ) {
        val page = userService.lambdaQuery()
                .like(StringUtils.hasText(searchName), User::getUsername, searchName)
                .page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page, UserVo.class));
    }

    @Operation(summary = "退出登入")
    @GetMapping("/logout/{id}")
    public R<Void> logout(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.logout(id);
        return R.ok();
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody UserForm userForm) {
        userService.addUser(userForm);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody UserForm userForm) {
        userService.modifyUser(userForm);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.removeUserById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "用户id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        userService.removeUserByIds(ids);
        return R.ok();
    }

    @Operation(summary = "修改密码")
    @PatchMapping("/pwd/change")
    public R<Void> changePwd(@Validated @RequestBody ChangePwdForm changePwdForm) {
        userService.modifyUserChangePwd(changePwdForm);
        return R.ok();
    }

    @Operation(summary = "重置密码")
    @PatchMapping("/pwd/reset/{id}")
    public R<Void> resetPwdById(@PathVariable("id") @Parameter(description = "用户id") Long id) {
        userService.modifyUserResetPwd(id);
        return R.ok();
    }
}

