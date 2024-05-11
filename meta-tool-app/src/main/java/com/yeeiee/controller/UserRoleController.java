package com.yeeiee.controller;

import com.yeeiee.entity.UserRole;
import com.yeeiee.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/userRole")
@Tag(name = "用户角色关系表 控制器")
public class UserRoleController extends BaseController<UserRoleService, UserRole> {

    public UserRoleController(UserRoleService service) {
        super(service);
    }
}

