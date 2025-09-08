package com.yeeiee.controller;

import com.yeeiee.domain.form.CodegenTableForm;
import com.yeeiee.domain.vo.CodegenTableVo;
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
    public R<List<CodegenTableVo>> getTableList(
            @RequestParam("dataSourceId") @Parameter(description = "数据源编号") Long dataSourceId,
            @RequestParam(value = "name", required = false) @Parameter(description = "表名") String name,
            @RequestParam(value = "comment", required = false) @Parameter(description = "表描述") String comment
    ) {
        val codegenTableVoList = codegenTableService.getTableList(dataSourceId, name, comment);
        return R.ok(codegenTableVoList);
    }


    @Operation(summary = "同步代码生成表")
    @PostMapping
    public void syncTable(@RequestBody @Validated CodegenTableForm codegenTableForm){
        codegenTableService.saveCodegenTable(codegenTableForm);
    }
}
