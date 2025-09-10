package com.yeeiee.controller;

import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.CodegenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "获取代码生成表列表")
    @GetMapping
    public R<List<CodegenTableSelectorVo>> getTableList(
            @RequestParam("dataSourceId") @Parameter(description = "数据源编号") Long dataSourceId
    ) {
        val codegenTableVoList = codegenTableService.getTableList(dataSourceId);
        return R.ok(codegenTableVoList);
    }


    @Operation(summary = "导入代码生成表")
    @PostMapping
    public R<Void> importCodegenTable(@RequestBody @Validated CodegenTableImportForm codegenTableImportForm){
        codegenTableService.addCodegenTable(codegenTableImportForm);
        return R.ok();
    }
}
