package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户权限关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
@RestController
@RequestMapping("/userAuth")
@Tag(name = "用户权限关系表 控制器")
public class UserAuthController extends BaseController<UserAuth> {

    public UserAuthController(IService<UserAuth> service) {
        super(service);
    }
}

