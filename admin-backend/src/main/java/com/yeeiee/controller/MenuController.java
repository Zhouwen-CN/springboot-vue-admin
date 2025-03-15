package com.yeeiee.controller;

import com.yeeiee.entity.Menu;
import com.yeeiee.entity.vo.MenuVo;
import com.yeeiee.service.MenuService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @Operation(summary = "查询菜单列表")
    @GetMapping
    public R<List<MenuVo>> getMenuList(@RequestParam("ids") Collection<Long> ids) {
        val menuList = menuService.getMenuList(ids);
        return R.ok(menuList);
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    public R<Void> addMenu(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return R.ok();
    }

    @Operation(summary = "更新菜单")
    @PutMapping
    public R<Void> modifyMenu(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public R<Void> removeMenuById(@PathVariable("id") Long id) {
        menuService.removeMenu(id);
        return R.ok();
    }
}

