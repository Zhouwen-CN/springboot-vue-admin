package com.yeeiee.controller;

import com.yeeiee.entity.Menu;
import com.yeeiee.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单表 控制器")
public class MenuController extends BaseController<MenuService, Menu> {

    public MenuController(MenuService service) {
        super(service);
    }
}

