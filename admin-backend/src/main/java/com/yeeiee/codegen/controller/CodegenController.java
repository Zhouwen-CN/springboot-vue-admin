package com.yeeiee.codegen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.SpringbootVueAdmin;
import com.yeeiee.codegen.domain.entity.CodegenColumn;
import com.yeeiee.codegen.domain.entity.CodegenTable;
import com.yeeiee.codegen.domain.form.CodegenTableColumnsForm;
import com.yeeiee.codegen.domain.form.CodegenTableImportForm;
import com.yeeiee.codegen.domain.vo.CodegenColumnVo;
import com.yeeiee.codegen.domain.vo.CodegenPreviewVo;
import com.yeeiee.codegen.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.codegen.domain.vo.CodegenTableVo;
import com.yeeiee.codegen.service.CodegenColumnService;
import com.yeeiee.codegen.service.CodegenTableService;
import com.yeeiee.codegen.service.freemarker.FreemarkerEngineService;
import com.yeeiee.system.domain.vo.PageVo;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.utils.BeanUtil;
import com.yeeiee.utils.ResponseObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private final FreemarkerEngineService freemarkerEngineService;

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<CodegenTableVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "keyword", required = false) @Parameter(description = "表名称 or 表备注") String keyword
    ) {
        // 尽量不要在xml sql中使用函数，因为函数不够通用
        val queryWrapper = Wrappers.<CodegenTable>lambdaQuery()
                .like(StringUtils.hasText(keyword), CodegenTable::getTableName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), CodegenTable::getTableComment, keyword);
        val list = codegenTableService.getCodegenTablePage(Page.of(current, size), queryWrapper);
        return R.ok(PageVo.fromPage(list));
    }

    @Operation(summary = "选择器查询")
    @GetMapping
    public R<List<CodegenTableSelectorVo>> getSelectorList(@RequestParam("dataSourceId") @Parameter(description = "数据源编号") Long dataSourceId) {
        val codegenTableVoList = codegenTableService.getCodegenTableSelector(dataSourceId);
        return R.ok(codegenTableVoList);
    }


    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@RequestBody @Validated CodegenTableImportForm codegenTableImportForm) {
        codegenTableService.addCodegenTable(codegenTableImportForm);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@RequestBody @Validated CodegenTableColumnsForm codegenTableColumnsForm) {
        codegenTableService.modifyCodegenConfig(codegenTableColumnsForm);
        return R.ok();
    }

    @Operation(summary = "同步")
    @GetMapping("/sync/{id}")
    public R<Void> syncColumnList(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        codegenTableService.modifyCodegenColumnList(id);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        codegenColumnService.remove(
                Wrappers.<CodegenColumn>lambdaQuery()
                        .eq(CodegenColumn::getTableId, id)
        );
        codegenTableService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "代码生成表id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        codegenColumnService.remove(
                Wrappers.<CodegenColumn>lambdaQuery()
                        .in(CodegenColumn::getTableId, ids)
        );
        codegenTableService.removeByIds(ids);
        return R.ok();
    }

    @Operation(summary = "id查询")
    @GetMapping("/{id}")
    public R<List<CodegenColumnVo>> getColumnListByTableId(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        val list = codegenColumnService.lambdaQuery()
                .eq(CodegenColumn::getTableId, id)
                .orderByAsc(CodegenColumn::getSortId)
                .list();
        val columnVoList = BeanUtil.toBean(list, CodegenColumnVo.class);
        return R.ok(columnVoList);
    }

    @Operation(summary = "预览")
    @GetMapping("/preview/{id}")
    public R<List<CodegenPreviewVo>> preview(@PathVariable("id") @Parameter(description = "代码生成表id") Long id) {
        val map = freemarkerEngineService.codegenById(id);
        val previewVoList = CodegenPreviewVo.fromMap(map);
        return R.ok(previewVoList);
    }

    @Operation(summary = "下载")
    @GetMapping("/download")
    public void download(
            @RequestParam("ids") @Parameter(description = "代码生成id列表") @Size(min = 1, max = 10) Collection<Long> ids,
            HttpServletResponse response
    ) {
        val map = freemarkerEngineService.codegenByIds(ids);
        val paths = new ArrayList<>(map.keySet());
        val ins = map.values().stream()
                .map(content -> {
                    val bytes = content.getBytes(StandardCharsets.UTF_8);
                    return new ByteArrayInputStream(bytes);
                }).toList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SpringbootVueAdmin.ZipUtil.zip(outputStream, paths, ins);

        ResponseObjectUtil.writeAttachment(response, "codegen.zip", outputStream.toByteArray());
    }
}
