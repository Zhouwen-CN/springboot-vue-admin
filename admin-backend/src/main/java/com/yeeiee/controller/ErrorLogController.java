package com.yeeiee.controller;

import com.yeeiee.entity.ErrorLog;
import com.yeeiee.service.ErrorLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 错误日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/errorLog")
@Tag(name = "错误日志表 控制器")
public class ErrorLogController extends BaseController<ErrorLogService, ErrorLog> {

    public ErrorLogController(ErrorLogService service) {
        super(service);
    }

    @GetMapping("/test")
    public String test() {
        throw new RuntimeException("test");
    }
}

