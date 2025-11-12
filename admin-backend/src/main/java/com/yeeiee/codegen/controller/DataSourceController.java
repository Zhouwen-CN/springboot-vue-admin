package com.yeeiee.codegen.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.codegen.domain.entity.DataSource;
import com.yeeiee.codegen.domain.form.DataSourceForm;
import com.yeeiee.codegen.domain.vo.DataSourceSelectorVo;
import com.yeeiee.codegen.domain.vo.DataSourceVo;
import com.yeeiee.codegen.service.DataSourceService;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.PageVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<DataSourceVo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current,
            @RequestParam(value = "searchName", required = false) @Parameter(description = "数据源名称") String searchName
    ) {
        val page = dataSourceService.lambdaQuery()
                .eq(StringUtils.hasText(searchName), DataSource::getName, searchName)
                .page(Page.of(current, size));

        return R.ok(PageVo.fromPage(page, DataSourceVo.class));
    }

    @Operation(summary = "选择器查询")
    @GetMapping
    public R<List<DataSourceSelectorVo>> getSelectorList() {
        val list = dataSourceService.lambdaQuery()
                .select(
                        DataSource::getId,
                        DataSource::getName
                ).list();

        val dataSourceSelectorVoList = BeanUtil.toBean(list, DataSourceSelectorVo.class);
        return R.ok(dataSourceSelectorVoList);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody DataSourceForm dataSourceForm) {
        val name = dataSourceForm.getName();
        val exists = dataSourceService.exists(
                Wrappers.<DataSource>lambdaQuery().eq(DataSource::getName, name)
        );

        if (exists) {
            throw new DmlOperationException("数据源名称已经存在");
        }

        val dataSource = BeanUtil.toBean(dataSourceForm, DataSource.class);
        dataSourceService.save(dataSource);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody DataSourceForm dataSourceForm) {
        val dataSource = BeanUtil.toBean(dataSourceForm, DataSource.class);
        dataSourceService.updateById(dataSource);
        return R.ok();
    }


    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "数据源id") Long id) {
        dataSourceService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "数据源id列表") @Size(min = 1, max = 10) Collection<Long> ids) {
        dataSourceService.removeByIds(ids);
        return R.ok();
    }

    @Operation(summary = "测试连接")
    @GetMapping("/check/{id}")
    public R<Void> checkConnection(@PathVariable("id") @Parameter(description = "数据源id") Long id) {
        val dataSource = dataSourceService.getById(id);
        try (val connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword())) {
            if (connection.isValid(2)) {
                return R.ok();
            }
        } catch (SQLException e) {
            return R.error(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(e));
        }

        return R.error(HttpStatus.BAD_REQUEST, "测试连接失败");
    }
}
