package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.vo.DataSourceVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据源配置表 前端控制器
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/datasource")
@Tag(name = "数据源配置表 控制器")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Operation(summary = "查询数据源配置分页")
    @GetMapping("/{size}/{current}")
    public R<PageVo<DataSourceVo>> page(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "name", required = false) @Parameter(description = "数据源名称") String name
    ){

        IPage<DataSourceVo> list = dataSourceService.getDataSourcePage(Page.of(current, size), name);
        return R.ok(PageVo.fromPage(list));
    }
}
