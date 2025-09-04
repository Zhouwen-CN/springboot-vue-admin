package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.domain.form.DataSourceForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.DataSourceVo;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
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

import java.sql.DriverManager;
import java.util.Collection;

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
    public R<PageVo<DataSourceVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "searchName", required = false) @Parameter(description = "数据源名称") String searchName
    ) {

        IPage<DataSourceVo> list = dataSourceService.getDataSourcePage(Page.of(current, size), searchName);
        return R.ok(PageVo.fromPage(list));
    }

    @Operation(summary = "新增数据源配置")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody DataSourceForm dataSourceForm) {
        val name = dataSourceForm.getName();
        val exists = dataSourceService.exists(
                Wrappers.<DataSource>lambdaQuery().eq(DataSource::getName, name)
        );

        if(exists){
            throw new DmlOperationException("数据源名称已经存在");
        }

        dataSourceService.save(dataSourceForm.toBean());
        return R.ok();
    }

    @Operation(summary = "更新数据源配置")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody DataSourceForm dataSourceForm) {
        dataSourceService.updateById(dataSourceForm.toBean());
        return R.ok();
    }


    @Operation(summary = "删除数据源配置")
    @DeleteMapping("/{id}")
    public R<Void> removeByid(@PathVariable("id") @Parameter(description = "数据源id") Long id) {
        dataSourceService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除数据源配置")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "数据源id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        dataSourceService.removeByIds(ids);
        return R.ok();
    }

    @Operation(summary = "测试数据源连接")
    @GetMapping("/check/{id}")
    public R<Void> checkConnection(@PathVariable("id") @Parameter(description = "数据源id") Long id) {
        val dataSource = dataSourceService.getById(id);
        // 测试jdbc连接
        try (val connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword())) {
            if (connection.isValid(2)) {
                return R.ok();
            }
        } catch (Exception e) {
            return R.error(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(e));
        }

        return R.error(HttpStatus.BAD_REQUEST, "测试连接失败");
    }
}
