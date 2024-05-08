package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
@RestController
@Tag(name = "用户表 控制器")
@RequestMapping("/user")
public class UserController extends BaseController<User> {

    public UserController(IService<User> service) {
        super(service);
    }
}

