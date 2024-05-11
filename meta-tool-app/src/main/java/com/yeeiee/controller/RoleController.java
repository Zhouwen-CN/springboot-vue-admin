package com.yeeiee.controller;

import com.yeeiee.entity.Role;
import com.yeeiee.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色表 控制器")
public class RoleController extends BaseController<RoleService, Role> {

    public RoleController(RoleService service) {
        super(service);
    }
}

