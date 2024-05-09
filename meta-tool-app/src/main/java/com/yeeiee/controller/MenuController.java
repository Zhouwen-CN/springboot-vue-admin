package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.Menu;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 二级菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RestController
@RequestMapping("/menu")
@Tag(name = "二级菜单表 控制器")
public class MenuController extends BaseController<Menu> {

    public MenuController(IService<Menu> service) {
        super(service);
    }
}

