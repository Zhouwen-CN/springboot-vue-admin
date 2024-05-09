package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.Role;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色表 控制器")
public class RoleController extends BaseController<Role> {

    public RoleController(IService<Role> service) {
        super(service);
    }
}

