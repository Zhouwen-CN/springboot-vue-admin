package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.domain.vo.CodegenTableVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.CodegenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/codegen")
public class CodegenController {

    private final CodegenTableService codegenTableService;

    @Operation(summary = "查询代码生成表分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<CodegenTableVo>> getCodegenTablePage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "searchName", required = false) @Parameter(description = "表名称") String searchName
    ) {
        IPage<CodegenTableVo> list = codegenTableService.getCodegenTablePage(Page.of(current, size), searchName);
        return R.ok(PageVo.fromPage(list));
    }

    @Operation(summary = "查询代码生成表选择器")
    @GetMapping
    public R<List<CodegenTableSelectorVo>> getCodegenTableSelector(
            @RequestParam("dataSourceId") @Parameter(description = "数据源编号") Long dataSourceId
    ) {
        val codegenTableVoList = codegenTableService.getCodegenTableSelector(dataSourceId);
        return R.ok(codegenTableVoList);
    }


    @Operation(summary = "导入代码生成表")
    @PostMapping
    public R<Void> importCodegenTable(@RequestBody @Validated CodegenTableImportForm codegenTableImportForm) {
        codegenTableService.addBatchCodegenTable(codegenTableImportForm);
        return R.ok();
    }

    @Operation(summary = "同步代码生成列")
    @GetMapping("/sync/{id}")
    public R<Void> syncCodegenColumnList(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        codegenTableService.modifyCodegenColumnList(id);
        return R.ok();
    }
}
