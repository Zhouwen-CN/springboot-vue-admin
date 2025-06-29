package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.ErrorLog;
import com.yeeiee.domain.entity.LoginLog;
import com.yeeiee.domain.entity.OperationLog;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.OperationLogService;
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
    public R<PageVo<LoginLog>> getLoginLogPage(
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
            lambdaQueryWrapper.eq(LoginLog::getOperation, operation);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(LoginLog::getStatus, status);
        }
        lambdaQueryWrapper.orderByDesc(LoginLog::getCreateTime);

        val page = loginLogService.page(new Page<>(current, size), lambdaQueryWrapper);
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "操作日志分页")
    @GetMapping("/ops/{size}/{current}")
    public R<PageVo<OperationLog>> getOperationLogPage(
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
            lambdaQueryWrapper.eq(OperationLog::getStatus, status);
        }
        lambdaQueryWrapper.orderByDesc(OperationLog::getCreateTime);

        val page = operationLogService.page(new Page<>(current, size), lambdaQueryWrapper);
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "异常日志分页")
    @GetMapping("/error/{size}/{current}")
    public R<PageVo<ErrorLog>> getErrorLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(required = false, name = "username") @Parameter(description = "用户名称") String username
    ) {

        val lambdaQueryWrapper = new LambdaQueryWrapper<ErrorLog>();
        if (StringUtils.hasText(username)) {
            lambdaQueryWrapper.like(ErrorLog::getUsername, username);
        }
        lambdaQueryWrapper.orderByDesc(ErrorLog::getCreateTime);

        val page = errorLogService.page(new Page<>(current, size), lambdaQueryWrapper);
        return R.ok(PageVo.fromPage(page));
    }
}

