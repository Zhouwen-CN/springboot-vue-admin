package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.DictData;
import com.yeeiee.entity.vo.DictDataVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.service.DictDataService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict/data")
@Tag(name = "字典数据表 控制器")
public class DictDataController {

    private final DictDataService dictDataService;

    @Operation(summary = "查询字典类型列表")
    @GetMapping("/{size}/{current}")
    public R<Page<DictData>> getPageByTypeId(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam("typeId") @Parameter(description = "字典类型id") Long typeId,
            @RequestParam(value = "label", required = false) @Parameter(description = "搜索标签键") String label) {
        val lambdaUpdateWrapper = Wrappers.lambdaUpdate(DictData.class)
                .eq(DictData::getTypeId, typeId);

        if (StringUtils.hasText(label)) {
            lambdaUpdateWrapper.like(DictData::getLabel, label);
        }
        lambdaUpdateWrapper.orderByAsc(DictData::getSort);

        val page = dictDataService.page(new Page<>(current, size), lambdaUpdateWrapper);
        return R.ok(page);
    }

    @Operation(summary = "新增字典数据")
    @PostMapping
    public R<Void> addData(@RequestBody DictData dictData) {
        val exists = dictDataService.exists(
                Wrappers.<DictData>lambdaQuery()
                        .eq(DictData::getTypeId, dictData.getTypeId())
                        .and(c1 -> c1.eq(DictData::getLabel, dictData.getLabel())
                                .or(c2 -> c2.eq(DictData::getValue, dictData.getValue()))
                        )
        );

        if (exists) {
            throw new DmlOperationException("标签键或标签值已存在");
        }

        dictDataService.save(dictData);
        return R.ok();
    }

    @Operation(summary = "修改字典数据")
    @PutMapping
    public R<Void> modifyData(@RequestBody DictData dictData) {
        dictDataService.updateById(dictData);
        return R.ok();
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/{id}")
    public R<Void> removeDataById(@PathVariable("id") Long id) {
        dictDataService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除字典数据")
    @DeleteMapping
    public R<Void> removeDataByIds(@RequestParam("ids") @Parameter(description = "需要删除的id列表") Collection<Long> ids) {
        dictDataService.removeByIds(ids);
        return R.ok();
    }

    // todo: 后续考虑添加缓存
    @Operation(summary = "根据类型查询字典列表")
    @GetMapping
    public R<List<DictDataVo>> getDataByType(@RequestParam("type") @Parameter(description = "字典类型") String type) {
        val list = dictDataService.getListByType(type);
        return R.ok(list);
    }
}
