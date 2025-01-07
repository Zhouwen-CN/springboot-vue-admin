package com.yeeiee.controller;

import com.yeeiee.entity.OperationLog;
import com.yeeiee.service.OperationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/operationLog")
@Tag(name = "操作日志表 控制器")
public class OperationLogController extends BaseController<OperationLogService, OperationLog> {

    public OperationLogController(OperationLogService service) {
        super(service);
    }
}

