<#--@formatter:off-->
package ${package.controller};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.entity}.${table.className};
import ${package.form}.${table.className}Form;
import ${package.base}.domain.validate.GroupingValidate;
import ${package.vo}.PageVo;
import ${package.vo}.R;
import ${package.service}.${table.className}Service;
import ${package.base}.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
import java.util.List;

/**
 * <p>
 * ${table.tableComment} 控制器
 * </p>
 *
 * @author ${table.author}
 * @since ${.now?string('yyyy-MM-dd')}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/${table.businessName}")
@Tag(name = "${table.tableComment?remove_ending('表')} 控制器")
public class ${table.className}Controller {
    <#-- service变量名 -->
    <#assign serviceName = table.className?uncap_first + 'Service'>
    <#-- 主键 -->
    <#list columns as column>
        <#if column.primaryKey>
            <#assign primaryKey = column>
        </#if>
    </#list>
    private final ${table.className}Service ${serviceName};

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<${table.className}>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size,
            @PathVariable("current") @Parameter(description = "当前页面") Integer current
    ) {
        val page = ${serviceName}.page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page));
    }

    @Operation(summary = "列表查询")
    @GetMapping
    public R<List<${table.className}>> getList() {
        val list = ${serviceName}.list();
        return R.ok(list);
    }

    @Operation(summary = "按照id查询")
    @GetMapping("/{id}")
    public R<${table.className}> getById(@PathVariable("id") @Parameter(description = "id") ${primaryKey.javaType} id) {
        val one = ${serviceName}.getById(id);
        return R.ok(one);
    }

    @Operation(summary = "新增")
    @PostMapping
    public R<Void> add(@RequestBody @Validated(GroupingValidate.Create.class) ${table.className}Form form) {
        val entity = BeanUtil.toBean(form, ${table.className}.class);
        ${serviceName}.save(entity);
        return R.ok();
    }

    @Operation(summary = "更新")
    @PutMapping
    public R<Void> modify(@RequestBody @Validated(GroupingValidate.Update.class) ${table.className}Form form) {
        val entity = BeanUtil.toBean(form, ${table.className}.class);
        ${serviceName}.updateById(entity);
        return R.ok();
    }

    @Operation(summary = "按照id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "id") Long id) {
        ${serviceName}.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "id列表") @Size(min = 1, max = 10) Collection<${primaryKey.javaType}> ids) {
        ${serviceName}.removeByIds(ids);
        return R.ok();
    }
}