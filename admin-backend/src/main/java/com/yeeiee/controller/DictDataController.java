package com.yeeiee.controller;

import com.yeeiee.entity.DictData;
import com.yeeiee.service.DictDataService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping
    public R<List<DictData>> getByTypeId(@RequestParam("typeId") @Parameter(description = "字典类型id") Long typeId) {
        val list = dictDataService.lambdaQuery()
                .eq(DictData::getTypeId, typeId)
                .list();

        return R.ok(list);
    }
}
