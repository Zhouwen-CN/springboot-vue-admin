package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.cache.DictCacheManager;
import com.yeeiee.domain.entity.DictType;
import com.yeeiee.domain.form.DictTypeForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.DictTypeVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.DictTypeService;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.Collections;

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
    public R<PageVo<DictTypeVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "keyword", required = false) @Parameter(description = "关键字") String keyword
    ) {
        val page = dictTypeService.lambdaQuery()
                .like(StringUtils.hasText(keyword), DictType::getName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), DictType::getDictType, keyword)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page, DictTypeVo.class));
    }

    @Operation(summary = "新增字典类型")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody DictTypeForm dictTypeForm) {
        dictTypeService.addDictType(dictTypeForm);
        return R.ok();
    }

    @Operation(summary = "更新字典类型")
    @PutMapping
    @CacheEvict(cacheNames = DictCacheManager.DICT_CACHE, key = "#p0.id")
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody DictTypeForm dictTypeForm) {
        val dictType = BeanUtil.toBean(dictTypeForm, DictType.class);
        dictTypeService.updateById(dictType);
        return R.ok();
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "字典类型id") Long id) {
        dictTypeService.removeDictTypeByIds(Collections.singleton(id));
        return R.ok();
    }

    @Operation(summary = "批量删除字典类型")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "字典类型id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        dictTypeService.removeDictTypeByIds(ids);
        return R.ok();
    }
}
