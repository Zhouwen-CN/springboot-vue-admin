package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.Job;
import com.yeeiee.domain.form.JobEnableChangeForm;
import com.yeeiee.domain.form.JobForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.JobSelectorVo;
import com.yeeiee.domain.vo.JobVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.scheduler.JobHandlerHolder;
import com.yeeiee.service.JobService;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 定时任务表 控制器
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/job")
@Tag(name = "定时任务 控制器")
public class JobController {
    private final JobService jobService;
    private final JobHandlerHolder jobHandlerHolder;

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<JobVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size
            , @PathVariable("current") @Parameter(description = "当前页面") Integer current
            , @RequestParam(value = "name", required = false) @Parameter(description = "任务名称") String name
            , @RequestParam(value = "jobEnable", required = false) @Parameter(description = "是否启用") Boolean jobEnable
    ) {
        val page = jobService.lambdaQuery()
                .like(StringUtils.hasText(name), Job::getName, name)
                .eq(Objects.nonNull(jobEnable), Job::getJobEnable, jobEnable)
                .page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page, JobVo.class));
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@RequestBody @Validated(GroupingValidate.Create.class) JobForm form) {
        jobService.addJob(form);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@RequestBody @Validated(GroupingValidate.Update.class) JobForm form) {
        jobService.modifyJob(form);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(
            @PathVariable("id") @Parameter(description = "id") Long id,
            @RequestParam("name") @Parameter(description = "任务名称") String name
    ) {
        jobService.removeJob(id, name);
        return R.ok();
    }

    @Operation(summary = "修改状态")
    @PatchMapping("/{id}")
    public R<Void> modifyJobEnable(
            @PathVariable("id") @Parameter(description = "id") Long id,
            @RequestBody @Validated JobEnableChangeForm jobEnableChangeForm
    ) {
        jobService.modifyJobEnable(id, jobEnableChangeForm);
        return R.ok();
    }

    @Operation(summary = "触发任务")
    @GetMapping("/trigger/{id}")
    public R<Void> triggerJob(@PathVariable("id") @Parameter(description = "id") Long id) {
        jobService.triggerJob(id);
        return R.ok();
    }

    @Operation(summary = "获取处理器名称")
    @GetMapping("/handlers")
    public R<List<String>> getJobHandlerNames(){
        val jobHandlerNames = jobHandlerHolder.getJobHandlerNames();
        return R.ok(jobHandlerNames);
    }

    @Operation(summary = "选择器查询")
    @GetMapping
    public R<List<JobSelectorVo>> getJobSelector(){
        val list = jobService.lambdaQuery()
                .select(Job::getId, Job::getName)
                .list();
        val voList = BeanUtil.toBean(list, JobSelectorVo.class);
        return R.ok(voList);
    }
}