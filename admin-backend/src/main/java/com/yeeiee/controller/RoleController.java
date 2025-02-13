package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.dto.RoleMenuIdsDto;
import com.yeeiee.entity.vo.RoleMenuVo;
import com.yeeiee.entity.vo.RoleVo;
import com.yeeiee.service.RoleService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
    public R<IPage<RoleMenuVo>> getRolePages(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                             @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                             @RequestParam(name = "searchName", required = false) @Parameter(description = "搜索用户名称") String searchName) {
        val list = roleService.getRolePages(new Page<>(current, size), searchName);
        return R.ok(list);
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
    public R<String> addRole(@RequestBody RoleMenuIdsDto roleMenuIdsDto) {
        roleService.addRole(roleMenuIdsDto);
        return R.ok();
    }

    @Operation(summary = "更新角色")
    @PutMapping
    public R<String> modifyRole(@RequestBody RoleMenuIdsDto roleMenuIdsDto) {
        roleService.modifyRole(roleMenuIdsDto);
        return R.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public R<String> removeRoleById(@PathVariable("id") Long id) {
        roleService.removeRoleById(id);
        return R.ok();
    }


    @Operation(summary = "批量删除角色")
    @DeleteMapping
    public R<String> removeRoleByIds(@RequestBody Collection<Long> ids) {
        roleService.removeRoleByIds(ids);
        return R.ok();
    }
}

