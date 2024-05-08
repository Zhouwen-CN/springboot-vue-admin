package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "权限表 控制器")
public class AuthController extends BaseController<Auth> {

    public AuthController(IService<Auth> service) {
        super(service);
    }
}

