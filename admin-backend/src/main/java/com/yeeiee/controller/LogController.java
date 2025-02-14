package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.ErrorLog;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.entity.OperationLog;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.OperationLogService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author chen
 * @since 2025-01-16
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
@Tag(name = "日志表 控制器")
public class LogController {

    private final LoginLogService loginLogService;
    private final OperationLogService operationLogService;
    private final ErrorLogService errorLogService;

    @Operation(summary = "登入日志分页")
    @GetMapping("/login/{size}/{current}")
    public R<Page<LoginLog>> loginLogPage(
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
            lambdaQueryWrapper.eq(LoginLog::getStatus, OperationStatusEnum.getStatus(status));
        }

        val page = loginLogService.page(new Page<>(current, size), lambdaQueryWrapper.orderByDesc(LoginLog::getCreateTime));
        return R.ok(page);
    }

    @Operation(summary = "操作日志分页")
    @GetMapping("/ops/{size}/{current}")
    public R<Page<OperationLog>> operationLogPage(
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
            lambdaQueryWrapper.eq(OperationLog::getStatus, OperationStatusEnum.getStatus(status));
        }

        val page = operationLogService.page(new Page<>(current, size), lambdaQueryWrapper.orderByDesc(OperationLog::getCreateTime));
        return R.ok(page);
    }

    @Operation(summary = "异常日志分页")
    @GetMapping("/error/{size}/{current}")
    public R<Page<ErrorLog>> errorLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current
    ) {
        val lambdaQueryWrapper = new LambdaQueryWrapper<ErrorLog>();
        lambdaQueryWrapper.orderByDesc(ErrorLog::getCreateTime);

        val page = errorLogService.page(new Page<>(current, size), lambdaQueryWrapper);
        return R.ok(page);
    }
}

