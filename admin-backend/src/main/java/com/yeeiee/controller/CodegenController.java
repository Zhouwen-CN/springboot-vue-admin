package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.form.CodegenTableColumnsForm;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenColumnVo;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.domain.vo.CodegenTableVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.CodegenColumnService;
import com.yeeiee.service.CodegenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
import java.util.List;

/**
 * <p>
 * 代码生成配置表 控制器
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/codegen")
@Tag(name = "代码生成配置表 控制器")
public class CodegenController {

    private final CodegenTableService codegenTableService;
    private final CodegenColumnService codegenColumnService;

    @Operation(summary = "查询代码生成表分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<CodegenTableVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "keyword", required = false) @Parameter(description = "表名称 or 表备注") String keyword
    ) {
        val list = codegenTableService.getCodegenTablePage(Page.of(current, size), keyword);
        return R.ok(PageVo.fromPage(list));
    }

    @Operation(summary = "查询代码生成表选择器")
    @GetMapping
    public R<List<CodegenTableSelectorVo>> getSelectorList(@RequestParam("dataSourceId") @Parameter(description = "数据源编号") Long dataSourceId) {
        val codegenTableVoList = codegenTableService.getCodegenTableSelector(dataSourceId);
        return R.ok(codegenTableVoList);
    }


    @Operation(summary = "导入代码生成表")
    @PostMapping
    public R<Void> addBatch(@RequestBody @Validated CodegenTableImportForm codegenTableImportForm) {
        codegenTableService.addBatchCodegenTable(codegenTableImportForm);
        return R.ok();
    }

    @Operation(summary = "修改代码生成表")
    @PutMapping
    public R<Void> modify(@RequestBody @Validated CodegenTableColumnsForm codegenTableColumnsForm) {
        codegenTableService.modifyCodegenConfig(codegenTableColumnsForm);
        return R.ok();
    }

    @Operation(summary = "同步代码生成表")
    @GetMapping("/sync/{id}")
    public R<Void> syncColumnList(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        codegenTableService.modifyCodegenColumnList(id);
        return R.ok();
    }

    @Operation(summary = "删除代码生成表")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        codegenColumnService.remove(
                Wrappers.<CodegenColumn>lambdaQuery()
                        .eq(CodegenColumn::getTableId, id)
        );
        codegenTableService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除代码生成表")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "代码生成表id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        codegenColumnService.remove(
                Wrappers.<CodegenColumn>lambdaQuery()
                        .in(CodegenColumn::getTableId, ids)
        );
        codegenTableService.removeByIds(ids);
        return R.ok();
    }

    @Operation(summary = "查询代码生成字段列表")
    @GetMapping("/{id}")
    public R<List<CodegenColumnVo>> getColumnListByTableId(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        val columnVoList = codegenColumnService.getListByTableId(id);
        return R.ok(columnVoList);
    }
}
