package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.entity.DictType;
import com.yeeiee.domain.form.DictTypeForm;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.service.DictDataService;
import com.yeeiee.service.DictTypeService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final DictDataService dictDataService;

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
    public R<Void> addType(@Validated(DictTypeForm.Create.class) @RequestBody DictTypeForm dictTypeForm) {
        val exists = dictTypeService.lambdaQuery()
                .eq(DictType::getType, dictTypeForm.getType())
                .exists();

        if (exists) {
            throw new DmlOperationException("字典类型已存在");
        }
        dictTypeService.save(dictTypeForm.toBean());
        return R.ok();
    }

    @Operation(summary = "更新字典类型")
    @PutMapping
    public R<Void> modifyType(@Validated(DictTypeForm.Update.class) @RequestBody DictTypeForm dictTypeForm) {
        dictTypeService.updateById(dictTypeForm.toBean());
        return R.ok();
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/{id}")
    public R<Void> removeTypeById(@PathVariable("id") Long id) {
        val dictDataList = dictDataService.lambdaQuery()
                .eq(DictData::getTypeId, id)
                .list();

        if (!dictDataList.isEmpty()) {
            throw new DmlOperationException("删除失败，尚有字典数据依赖");
        }

        dictTypeService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除字典类型")
    @DeleteMapping
    public R<Void> removeTypeByIds(@RequestParam("ids") @Parameter(description = "需要删除的id列表") Collection<Long> ids) {
        val dictDataList = dictDataService.lambdaQuery()
                .in(DictData::getTypeId, ids)
                .list();

        if (!dictDataList.isEmpty()) {
            throw new DmlOperationException("删除失败，尚有字典数据依赖");
        }

        dictTypeService.removeByIds(ids);
        return R.ok();
    }
}
