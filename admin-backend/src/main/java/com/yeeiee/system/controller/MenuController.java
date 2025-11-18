package com.yeeiee.system.controller;

import com.yeeiee.system.domain.entity.Menu;
import com.yeeiee.system.domain.entity.RoleMenu;
import com.yeeiee.system.domain.form.MenuForm;
import com.yeeiee.system.domain.validate.GroupingValidate;
import com.yeeiee.system.domain.vo.MenuVo;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.service.MenuService;
import com.yeeiee.system.service.RoleMenuService;
import com.yeeiee.utils.BeanUtil;
import com.yeeiee.utils.SecurityUserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单表 控制器")
public class MenuController {
    private final MenuService menuService;
    private final RoleMenuService roleMenuService;

    @Operation(summary = "列表查询")
    @GetMapping
    public R<List<MenuVo>> getList() {
        val roleNames = SecurityUserUtil.getAuthorities();
        if(CollectionUtils.isEmpty(roleNames)){
            return R.ok(List.of());
        }
        val menuList = menuService.getMenuListByRoleNames(roleNames);
        return R.ok(menuList);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody MenuForm menuForm) {
        menuService.addMenu(menuForm);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody MenuForm menuForm) {
        val menu = BeanUtil.toBean(menuForm, Menu.class);
        menuService.updateById(menu);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "菜单id") Long id) {
        menuService.removeMenu(id);
        return R.ok();
    }

    @Operation(summary = "查询菜单id列表")
    @GetMapping("/{roleId}")
    public R<List<Long>> getIdsByRoleId(@PathVariable("roleId") @Parameter(description = "角色id") Long roleId) {
        val menuIds = roleMenuService.lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, roleId)
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .toList();
        return R.ok(menuIds);
    }
}

