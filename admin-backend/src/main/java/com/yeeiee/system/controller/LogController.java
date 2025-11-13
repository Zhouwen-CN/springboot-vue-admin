package com.yeeiee.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.scheduler.domain.vo.JobLogVo;
import com.yeeiee.scheduler.service.JobLogService;
import com.yeeiee.system.domain.entity.ErrorLog;
import com.yeeiee.system.domain.entity.LoginLog;
import com.yeeiee.system.domain.entity.OperationLog;
import com.yeeiee.system.domain.vo.PageVo;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.service.ErrorLogService;
import com.yeeiee.system.service.LoginLogService;
import com.yeeiee.system.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private final JobLogService jobLogService;

    @Operation(summary = "登入日志分页")
    @GetMapping("/login/{size}/{current}")
    public R<PageVo<LoginLog>> getLoginLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(required = false, name = "username") @Parameter(description = "用户名称") String username,
            @RequestParam(required = false, name = "operation") @Parameter(description = "操作类型") Integer operation,
            @RequestParam(required = false, name = "status") @Parameter(description = "状态") Integer status
    ) {
        val page = loginLogService.lambdaQuery()
                .like(StringUtils.hasText(username), LoginLog::getCreateUser, username)
                .eq(Objects.nonNull(operation), LoginLog::getOperation, operation)
                .eq(Objects.nonNull(status), LoginLog::getStatus, status)
                .orderByDesc(LoginLog::getCreateTime)
                .page(Page.of(current, size));

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
        val page = operationLogService.lambdaQuery()
                .like(StringUtils.hasText(username), OperationLog::getCreateUser, username)
                .eq(Objects.nonNull(status), OperationLog::getStatus, status)
                .orderByDesc(OperationLog::getCreateTime)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "异常日志分页")
    @GetMapping("/error/{size}/{current}")
    public R<PageVo<ErrorLog>> getErrorLogPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(required = false, name = "username") @Parameter(description = "用户名称") String username
    ) {
        val page = errorLogService.lambdaQuery()
                .like(StringUtils.hasText(username), ErrorLog::getCreateUser, username)
                .orderByDesc(ErrorLog::getCreateTime)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "定时任务分页")
    @GetMapping("/job/{size}/{current}")
    public R<PageVo<JobLogVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size
            , @PathVariable("current") @Parameter(description = "当前页面") Integer current
            , @RequestParam(value = "jobId", required = false) @Parameter(description = "任务编号") Long jobId
            , @RequestParam(value = "status", required = false) @Parameter(description = "任务状态") Integer status
            , @RequestParam(value = "startTime", required = false) @Parameter(description = "开始时间") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime
            , @RequestParam(value = "endTime", required = false) @Parameter(description = "结束时间")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {

        IPage<JobLogVo> page = jobLogService.getJobLogPage(
                Page.<JobLogVo>of(current, size),
                jobId,
                status,
                startTime,
                endTime
        );
        return R.ok(PageVo.fromPage(page));
    }
}

