package com.yeeiee.controller;

import com.yeeiee.entity.Menu;
import com.yeeiee.service.MenuService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单表 控制器")
public class MenuController {
    private MenuService menuService;

    @Operation(summary = "新增菜单")
    @PostMapping("")
    public R<Void> addMenu(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return R.ok();
    }

    @Operation(summary = "更新菜单")
    @PutMapping("")
    public R<Void> updateMenu(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public R<Void> deleteMenu(@PathVariable("id") @Parameter(description = "菜单id") Long id) {
        menuService.deleteMenu(id);
        return R.ok();
    }
}

