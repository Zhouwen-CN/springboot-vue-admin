package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.ErrorLog;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.entity.OperationLog;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StatusEnum;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/log")
@Tag(name = "日志表 控制器")
@AllArgsConstructor
public class LogController {

    private LoginLogService loginLogService;
    private OperationLogService operationLogService;
    private ErrorLogService errorLogService;

    @Operation(summary = "登入日志分页")
    @GetMapping("/login/{size}/{current}")
    public Page<LoginLog> loginLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(required = false, name = "username") @Parameter(description = "用户名称") String username,
            @RequestParam(required = false, name = "operation") @Parameter(description = "操作类型") Integer operation,
            @RequestParam(required = false, name = "status") @Parameter(description = "状态") Integer status
    ) {
        val lambdaQueryWrapper = new LambdaQueryWrapper<LoginLog>();

        if (StringUtils.hasText(username)) {
            lambdaQueryWrapper.like(LoginLog::getUsername, username);
        }

        if (operation != null) {
            lambdaQueryWrapper.eq(LoginLog::getOperation, LoginOperationEnum.getOperation(operation));
        }

        if (status != null) {
            lambdaQueryWrapper.eq(LoginLog::getStatus, StatusEnum.getStatus(status));
        }

        return loginLogService.page(new Page<>(current, size), lambdaQueryWrapper.orderByDesc(LoginLog::getCreateTime));
    }

    @Operation(summary = "操作日志分页")
    @GetMapping("/ops/{size}/{current}")
    public Page<OperationLog> operationLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(required = false, name = "username") @Parameter(description = "用户名称") String username,
            @RequestParam(required = false, name = "status") @Parameter(description = "状态") Integer status
    ) {
        val lambdaQueryWrapper = new LambdaQueryWrapper<OperationLog>();

        if (StringUtils.hasText(username)) {
            lambdaQueryWrapper.like(OperationLog::getUsername, username);
        }

        if (status != null) {
            lambdaQueryWrapper.eq(OperationLog::getStatus, StatusEnum.getStatus(status));
        }

        return operationLogService.page(new Page<>(current, size), lambdaQueryWrapper.orderByDesc(OperationLog::getCreateTime));
    }

    @Operation(summary = "异常日志分页")
    @GetMapping("/error/{size}/{current}")
    public Page<ErrorLog> errorLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current
    ) {
        val lambdaQueryWrapper = new LambdaQueryWrapper<ErrorLog>();
        lambdaQueryWrapper.orderByDesc(ErrorLog::getCreateTime);

        return errorLogService.page(new Page<>(current, size), lambdaQueryWrapper);
    }
}

