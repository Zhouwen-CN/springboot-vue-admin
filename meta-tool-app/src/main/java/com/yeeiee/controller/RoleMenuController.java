package com.yeeiee.controller;

import com.yeeiee.entity.RoleMenu;
import com.yeeiee.service.RoleMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户权限关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/roleMenu")
@Tag(name = "用户权限关系表 控制器")
public class RoleMenuController extends BaseController<RoleMenuService, RoleMenu> {

    public RoleMenuController(RoleMenuService service) {
        super(service);
    }
}

