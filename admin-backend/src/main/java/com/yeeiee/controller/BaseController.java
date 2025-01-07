package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public R<String> removeById(@PathVariable("id") Long id) {
        return R.check(service.removeById(id), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "新增")
    @PostMapping("")
    public R<String> save(@RequestBody D entity) {
        return R.check(service.save(entity), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "更新")
    @PutMapping("")
    public R<String> update(@RequestBody D entity) {
        return R.check(service.updateById(entity), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "新增或者更新")
    @PostMapping("/save")
    public R<String> saveOrUpdate(@RequestBody D entity) {
        return R.check(service.saveOrUpdate(entity), HttpStatus.BAD_REQUEST);
    }
}
