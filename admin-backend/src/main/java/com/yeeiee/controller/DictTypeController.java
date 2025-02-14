package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.DictType;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.service.DictTypeService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
    public R<Page<DictType>> getPage(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                                     @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                                     @RequestParam(value = "keyword", required = false) @Parameter(description = "关键字") String keyword) {
        val lambdaQueryWrapper = new LambdaQueryWrapper<DictType>();

        if (StringUtils.hasText(keyword)) {
            lambdaQueryWrapper.like(DictType::getName, keyword)
                    .or()
                    .like(DictType::getType, keyword);
        }

        val page = dictTypeService.page(new Page<>(current, size), lambdaQueryWrapper);
        return R.ok(page);
    }

    @Operation(summary = "新增字典类型")
    @PostMapping
    public R<String> addType(@RequestBody DictType dictType) {
        val exists = dictTypeService.lambdaQuery()
                .eq(DictType::getType, dictType.getType())
                .exists();

        if (exists) {
            throw new DmlOperationException("编码已存在");
        }
        dictTypeService.save(dictType);
        return R.ok();
    }

    @Operation(summary = "更新字典类型")
    @PutMapping
    public R<String> modifyType(@RequestBody DictType dictType) {
        dictTypeService.updateById(dictType);
        return R.ok();
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/{id}")
    public R<String> removeTypeById(@PathVariable("id") Long id) {
        dictTypeService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除字典类型")
    @DeleteMapping
    public R<String> removeTypeByIds(@RequestParam("ids") @Parameter(description = "需要删除的id列表") Long[] ids) {
        dictTypeService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
