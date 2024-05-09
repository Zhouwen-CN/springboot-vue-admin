package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.UserRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RestController
@RequestMapping("/userRole")
@Tag(name = "用户角色关系表 控制器")
public class UserRoleController extends BaseController<UserRole> {

    public UserRoleController(IService<UserRole> service) {
        super(service);
    }
}

