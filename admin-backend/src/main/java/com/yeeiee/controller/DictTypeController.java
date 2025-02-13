package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.DictType;
import com.yeeiee.service.DictTypeService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict/type")
@Tag(name = "字典类型表 控制器")
public class DictTypeController {

    private final DictTypeService dictTypeService;

    @Operation(summary = "查询字典类型分页")
    @GetMapping("/{size}/{current}")
    public R<Page<DictType>> getTypePage(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                         @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                         @RequestParam("name") @Parameter(description = "字典名称") String name,
                                         @RequestParam("type") @Parameter(description = "字典类型") String type) {
        val lambdaQuery = dictTypeService.lambdaQuery();

        if (StringUtils.hasText(name)) {
            lambdaQuery.like(DictType::getName, name);
        }

        if (StringUtils.hasText(type)) {
            lambdaQuery.like(DictType::getType, type);
        }

        val page = dictTypeService.page(new Page<>(current, size));
        return R.ok(page);
    }
}
