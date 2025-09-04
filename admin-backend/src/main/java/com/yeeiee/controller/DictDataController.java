package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.cache.DictCacheManager;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.DictDataVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
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
    private final DictCacheManager dictCacheManager;

    @Operation(summary = "查询字典数据分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<DictData>> getPageByTypeId(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam("typeId") @Parameter(description = "字典类型id") Long typeId,
            @RequestParam(value = "label", required = false) @Parameter(description = "搜索标签键") String label
    ) {
        val page = dictDataService.lambdaQuery()
                .eq(DictData::getTypeId, typeId)
                .like(StringUtils.hasText(label), DictData::getLabel, label)
                .orderByAsc(DictData::getSort)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "新增字典数据")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody DictDataForm dictDataForm) {
        dictDataService.addDictData(dictDataForm);
        return R.ok();
    }

    @Operation(summary = "修改字典数据")
    @PutMapping
    @CacheEvict(cacheNames = DictCacheManager.DICT_CACHE, key = "#p0.typeId")
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody DictDataForm dictDataForm) {
        dictDataService.updateById(dictDataForm.toBean());
        return R.ok();
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "字典数据id") Long id) {
        dictCacheManager.evictByDataIds(Collections.singleton(id));
        dictDataService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除字典数据")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "字典数据id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        dictCacheManager.evictByDataIds(ids);
        dictDataService.removeByIds(ids);
        return R.ok();
    }

    @Operation(summary = "根据类型id字典列表")
    @GetMapping("/{typeId}")
    public R<List<DictDataVo>> getListByTypeId(@PathVariable("typeId") @Parameter(description = "字典类型id") Long typeId) {
        val list = dictCacheManager.getDictByTypeId(typeId);
        return R.ok(list);
    }
}
