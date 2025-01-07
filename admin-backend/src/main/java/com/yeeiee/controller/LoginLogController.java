package com.yeeiee.controller;

import com.yeeiee.entity.LoginLog;
import com.yeeiee.service.LoginLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/loginLog")
@Tag(name = "登录日志表 控制器")
public class LoginLogController extends BaseController<LoginLogService, LoginLog> {

    public LoginLogController(LoginLogService service) {
        super(service);
    }
}

