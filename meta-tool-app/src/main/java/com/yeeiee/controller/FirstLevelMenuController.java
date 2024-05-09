package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.FirstLevelMenu;
import com.yeeiee.entity.dto.MenuDto;
import com.yeeiee.service.FirstLevelMenuService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 一级菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RestController
@RequestMapping("/m1")
@Tag(name = "一级菜单表 控制器")
public class FirstLevelMenuController extends BaseController<FirstLevelMenu> {

    public FirstLevelMenuController(IService<FirstLevelMenu> service) {
        super(service);
    }

    @GetMapping("/menus")
    public R<List<MenuDto>> getMenuDtoList() {
        val menus = ((FirstLevelMenuService) service).getMenus();
        return R.ok(menus);
    }
}

