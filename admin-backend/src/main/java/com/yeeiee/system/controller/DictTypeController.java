package com.yeeiee.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.cache.DictCacheManager;
import com.yeeiee.system.domain.entity.DictType;
import com.yeeiee.system.domain.form.DictTypeForm;
import com.yeeiee.system.domain.validate.GroupingValidate;
import com.yeeiee.system.domain.vo.DictTypeSelectorVo;
import com.yeeiee.system.domain.vo.DictTypeVo;
import com.yeeiee.system.domain.vo.PageVo;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.service.DictTypeService;
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
import java.util.List;

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

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<DictTypeVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "name", required = false) @Parameter(description = "关键字") String name
    ) {
        val page = dictTypeService.lambdaQuery()
                .like(StringUtils.hasText(name), DictType::getName, name)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page, DictTypeVo.class));
    }

    @Operation(summary = "选择器查询")
    @GetMapping
    public R<List<DictTypeSelectorVo>> getSelector(){
        val list = dictTypeService.lambdaQuery()
                .select(
                        DictType::getId,
                        DictType::getName
                )
                .eq(DictType::getDictEnable, true)
                .list();
        val dictTypeSelectorVoList = BeanUtil.toBean(list, DictTypeSelectorVo.class);
        return R.ok(dictTypeSelectorVoList);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody DictTypeForm dictTypeForm) {
        dictTypeService.addDictType(dictTypeForm);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    @CacheEvict(cacheNames = DictCacheManager.DICT_CACHE, key = "#p0.id")
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody DictTypeForm dictTypeForm) {
        val dictType = BeanUtil.toBean(dictTypeForm, DictType.class);
        dictTypeService.updateById(dictType);
        return R.ok();
    }

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "字典类型id") Long id) {
        dictTypeService.removeDictTypeByIds(Collections.singleton(id));
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "字典类型id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        dictTypeService.removeDictTypeByIds(ids);
        return R.ok();
    }
}
