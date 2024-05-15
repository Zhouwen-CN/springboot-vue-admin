package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 公共控制器
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
public abstract class BaseController<S extends IService<D>, D> {
    protected S service;

    public BaseController(S service) {
        this.service = service;
    }

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<Page<D>> page(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                           @PathVariable("current") @Parameter(description = "当前页面") Integer current) {
        val page = service.page(new Page<>(current, size));
        return R.ok(page);
    }

    @Operation(summary = "查询所有")
    @GetMapping("")
    public R<List<D>> getList() {
        val list = service.list();
        return R.ok(list);
    }

    @Operation(summary = "按照id查询")
    @GetMapping("/{id}")
    public R<D> getById(@PathVariable("id") Long id) {
        val one = service.getById(id);
        return R.ok(one);
    }

    @Operation(summary = "按照id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") Long id) {
        val status = service.removeById(id);
        if (!status) {
            throw new DmlOperationException("删除失败【id】：" + id);
        }
        return R.ok();
    }

    @Operation(summary = "按照id批量删除")
    @DeleteMapping("")
    public R<Void> removeByIds(@RequestBody Collection<?> ids) {
        val status = service.removeByIds(ids);
        if (!status) {
            throw new DmlOperationException("删除失败【ids】：" + ids);
        }
        return R.ok();
    }

    @Operation(summary = "新增")
    @PostMapping("")
    public R<Void> save(@RequestBody D entity) {
        val status = service.save(entity);
        if (!status) {
            throw new DmlOperationException("新增失败【entity】：" + entity);
        }
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping("")
    public R<Void> update(@RequestBody D entity) {
        val status = service.updateById(entity);
        if (!status) {
            throw new DmlOperationException("更新失败【entity】：" + entity);
        }
        return R.ok();
    }

    @Operation(summary = "新增或者更新")
    @PostMapping("/save")
    public R<Void> saveOrUpdate(@RequestBody D entity) {
        val status = service.saveOrUpdate(entity);
        if (!status) {
            throw new DmlOperationException("upsert失败【entity】：" + entity);
        }
        return R.ok();
    }
}
