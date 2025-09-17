package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.StudentTest;
import com.yeeiee.domain.form.StudentTestForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.StudentTestService;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 学生表-代码生成测试 控制器
 * </p>
 *
 * @author chen
 * @since 2025-09-17
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
@Tag(name = "学生表-代码生成测试 控制器")
public class StudentTestController {
    private final StudentTestService studentTestService;

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<StudentTest>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size
            , @PathVariable("current") @Parameter(description = "当前页面") Integer current
    ) {
        val page = studentTestService.page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "列表查询")
    @GetMapping
    public R<List<StudentTest>> getList() {
        val list = studentTestService.list();
        return R.ok(list);
    }

    @Operation(summary = "按照id查询")
    @GetMapping("/{id}")
    public R<StudentTest> getById(@PathVariable("id") @Parameter(description = "id") Long id) {
        val one = studentTestService.getById(id);
        return R.ok(one);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@RequestBody @Validated(GroupingValidate.Create.class) StudentTestForm form) {
        val entity = BeanUtil.toBean(form, StudentTest.class);
        studentTestService.save(entity);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@RequestBody @Validated(GroupingValidate.Update.class) StudentTestForm form) {
        val entity = BeanUtil.toBean(form, StudentTest.class);
        studentTestService.updateById(entity);
        return R.ok();
    }

    @Operation(summary = "按照id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "id") Long id) {
        studentTestService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        studentTestService.removeByIds(ids);
        return R.ok();
    }
}
