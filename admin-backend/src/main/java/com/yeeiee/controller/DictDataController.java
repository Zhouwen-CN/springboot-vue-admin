package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;
import com.yeeiee.domain.vo.DictDataVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.service.DictDataService;
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

    @Operation(summary = "查询字典数据分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<DictData>> getDictDataPageByTypeId(
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
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "新增字典数据")
    @PostMapping
    public R<Void> addDictData(@Validated(DictDataForm.Create.class) @RequestBody DictDataForm dictDataForm) {
        val exists = dictDataService.exists(
                Wrappers.<DictData>lambdaQuery()
                        .eq(DictData::getTypeId, dictDataForm.getTypeId())
                        .and(c1 -> c1.eq(DictData::getLabel, dictDataForm.getLabel())
                                .or(c2 -> c2.eq(DictData::getValue, dictDataForm.getValue()))
                        )
        );

        if (exists) {
            throw new DmlOperationException("标签键或标签值已存在");
        }

        dictDataService.save(dictDataForm.toBean());
        return R.ok();
    }

    @Operation(summary = "修改字典数据")
    @PutMapping
    public R<Void> modifyDictData(@Validated(DictDataForm.Update.class) @RequestBody DictDataForm dictDataForm) {
        dictDataService.updateById(dictDataForm.toBean());
        return R.ok();
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/{id}")
    public R<Void> removeDictDataById(@PathVariable("id") Long id) {
        dictDataService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除字典数据")
    @DeleteMapping
    public R<Void> removeDictDataByIds(@RequestParam("ids") @Parameter(description = "需要删除的id列表") Collection<Long> ids) {
        dictDataService.removeByIds(ids);
        return R.ok();
    }

    // todo: 后续考虑添加缓存
    @Operation(summary = "根据类型id字典列表")
    @GetMapping("/{typeId}")
    public R<List<DictDataVo>> getDictDataListByTypeId(@PathVariable("typeId") @Parameter(description = "字典类型id") Long typeId) {
        val list = dictDataService.getListByTypeId(typeId);
        return R.ok(list);
    }
}
