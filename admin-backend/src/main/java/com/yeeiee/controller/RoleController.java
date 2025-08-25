package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.form.RoleForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.domain.vo.RoleMenuVo;
import com.yeeiee.domain.vo.RoleVo;
import com.yeeiee.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "角色表 控制器")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "查询角色分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<RoleMenuVo>> getRolePage(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                             @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                             @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName) {
        val page = roleService.getRolePage(Page.of(current, size), searchName);
        return R.ok(PageVo.fromPage(page));
    }

    /**
     * 默认不会查询出admin角色
     */
    @Operation(summary = "查询角色列表")
    @GetMapping
    public R<List<RoleVo>> getRoleList() {
        return R.ok(roleService.getRoleVoList());
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public R<Void> addRole(@Validated(GroupingValidate.Create.class) @RequestBody RoleForm roleForm) {
        roleService.addRole(roleForm);
        return R.ok();
    }

    @Operation(summary = "更新角色")
    @PutMapping
    public R<Void> modifyRole(@Validated(GroupingValidate.Update.class) @RequestBody RoleForm roleForm) {
        roleService.modifyRole(roleForm);
        return R.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public R<Void> removeRoleById(@PathVariable("id") Long id) {
        roleService.removeRoleById(id);
        return R.ok();
    }


    @Operation(summary = "批量删除角色")
    @DeleteMapping
    public R<Void> removeRoleByIds(@RequestParam("ids") @Parameter(description = "需要删除的角色id列表") @Size(min = 1,max = 10) Collection<Long> ids) {
        roleService.removeRoleByIds(ids);
        return R.ok();
    }
}

