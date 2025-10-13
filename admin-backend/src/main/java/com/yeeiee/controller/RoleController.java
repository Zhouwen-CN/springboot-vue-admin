package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.Role;
import com.yeeiee.domain.form.RoleForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.domain.vo.RoleSelectorVo;
import com.yeeiee.domain.vo.RoleVo;
import com.yeeiee.service.RoleService;
import com.yeeiee.utils.BeanUtil;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
@Tag(name = "角色控制器")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<RoleVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName
    ) {
        val page = roleService.lambdaQuery()
                .like(StringUtils.hasText(searchName), Role::getRoleName, searchName)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page, RoleVo.class));
    }

    @Operation(summary = "选择器查询")
    @GetMapping
    public R<List<RoleSelectorVo>> getList() {
        val list = roleService.lambdaQuery()
                .select(Role::getId, Role::getRoleName)
                .list();
        val roleSelectorVoList = BeanUtil.toBean(list, RoleSelectorVo.class);
        return R.ok(roleSelectorVoList);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody RoleForm roleForm) {
        roleService.addRole(roleForm);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody RoleForm roleForm) {
        roleService.modifyRole(roleForm);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "角色id") Long id) {
        roleService.removeRoleById(id);
        return R.ok();
    }


    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "角色id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        roleService.removeRoleByIds(ids);
        return R.ok();
    }

    @Operation(summary = "查询角色id列表")
    @GetMapping("/{userId}")
    public R<List<Long>> getListByUserId(@PathVariable("userId") @Parameter(description = "用户id") Long userId) {
        val list = roleService.getRoleSelectorVoListByUserId(userId)
                .stream()
                .map(RoleSelectorVo::getId)
                .toList();
        return R.ok(list);
    }
}

