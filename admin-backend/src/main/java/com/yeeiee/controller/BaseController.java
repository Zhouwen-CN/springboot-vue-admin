package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 公共控制器，不建议生产环境使用
 * </p>
 *
 * @param <I> 主键类型
 * @param <D> entity类型
 * @param <S> service类型
 * @author chen
 * @since 2024-04-27
 */
@Deprecated
public abstract class BaseController<I extends Serializable, D, S extends IService<D>> implements BaseControllerHelper<D> {
    protected S service;

    public BaseController(S service) {
        this.service = service;
    }

    @Operation(summary = "查询分页")
    @GetMapping("/{size}/{current}")
    public R<Page<D>> getPage(@PathVariable("size") @Parameter(description = "页面大小") Integer size,
                              @PathVariable("current") @Parameter(description = "当前页面") Integer current,
                              HttpServletRequest request) {
        val page = service.page(new Page<>(current, size), pageHelper(request));
        return R.ok(page);
    }

    @Operation(summary = "查询列表")
    @GetMapping
    public R<List<D>> getList() {
        val list = service.list();
        return R.ok(list);
    }

    @Operation(summary = "按照id查询")
    @GetMapping("/{id}")
    public R<D> getById(@PathVariable("id") I id) {
        val one = service.getById(id);
        return R.ok(one);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@RequestBody D entity) {
        service.save(entity);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@RequestBody D entity) {
        service.updateById(entity);
        return R.ok();
    }

    @Operation(summary = "按照id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") I id) {
        service.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "需要删除的id列表") Collection<I> ids) {
        service.removeByIds(ids);
        return R.ok();
    }
}

