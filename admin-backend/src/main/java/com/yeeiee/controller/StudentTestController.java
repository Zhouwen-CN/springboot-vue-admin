package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.StudentTest;
import com.yeeiee.domain.form.StudentTestForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.domain.vo.StudentTestVo;
import com.yeeiee.service.StudentTestService;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

/**
 * <p>
 * 学生表-代码生成测试 控制器
 * </p>
 *
 * @author chen
 * @since 2025-09-18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
@Tag(name = "学生表-代码生成测试 控制器")
public class StudentTestController {
    private final StudentTestService studentTestService;

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<StudentTestVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size
            , @PathVariable("current") @Parameter(description = "当前页面") Integer current
            , @RequestParam(value = "name", required = false) @Parameter(description = "名称") String name
            , @RequestParam(value = "gender", required = false) @Parameter(description = "性别") Integer gender
    ) {
        val page = studentTestService.lambdaQuery()
                .like(StringUtils.hasText(name), StudentTest::getName, name)
                .eq(Objects.nonNull(gender), StudentTest::getGender, gender)
                .page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page, StudentTestVo.class));
    }

    // 可能会用不上
    @Operation(summary = "按照id查询")
    @GetMapping("/{id}")
    public R<StudentTestVo> getById(@PathVariable("id") @Parameter(description = "id") Long id) {
        val one = studentTestService.getById(id);
        val vo = BeanUtil.toBean(one, StudentTestVo.class);
        return R.ok(vo);
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