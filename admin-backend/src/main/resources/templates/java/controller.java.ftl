<#--@formatter:off-->
package ${package.controller};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.entity}.${table.className};
import ${package.form}.${table.className}Form;
import ${package.javaBasePackage}.domain.validate.GroupingValidate;
import ${package.vo}.PageVo;
import ${package.vo}.R;
import ${package.vo}.${table.className}Vo;
import ${package.service}.${table.className}Service;
import ${package.javaBasePackage}.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
import java.util.Objects;

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
    <#-- 主键字段 -->
    <#assign primaryColumn = {}>
    <#-- 更新字段 -->
    <#assign updateColumns = []>
    <#-- 分页查询 -->
    <#assign pageLambdaQuery = serviceName +'.lambdaQuery()'>
    <#-- 限定比较类型 -->
    <#assign comparableType = ['Byte','Short','Integer','Long','Float','Double','LocalDateTime']>
    <#-- lambda查询语句换行空格 -->
    <#assign prefixSpace='\n                '>
    <#-- 追加分页lambda查询语句函数 -->
    <#function appendPageQuery condition column>
        <#assign pageLambdaQuery = pageLambdaQuery + prefixSpace + '.'+ condition +'(Objects.nonNull('+ column.javaField +'), '+ table.className +'::get'+ column.javaField?cap_first +', '+ column.javaField +')'>
    </#function>
    <#-- 根据条件追加分页lambda查询语句函数 -->
    <#function appendPageQueryByCondition column>
        <#if column.selectCondition=='='>
            <#call appendPageQuery('eq', column)>
        <#elseif column.selectCondition=='>'>
            <#call appendPageQuery('gt', column)>
        <#elseif column.selectCondition=='>='>
            <#call appendPageQuery('ge', column)>
        <#elseif column.selectCondition=='<'>
            <#call appendPageQuery('lt', column)>
        <#elseif column.selectCondition=='<='>
            <#call appendPageQuery('le', column)>
        </#if>
    </#function>
    <#-- 获取分页请求参数函数 -->
    <#function getParameter column>
        <#return '@RequestParam(value = "' + column.javaField + '", required = false) @Parameter(description = "' + column.columnComment + '") ' + column.javaType + ' ' + column.javaField>
    </#function>
    <#list columns as column>
        <#if column.primaryKey>
            <#assign primaryColumn = column>
        </#if>
        <#if column.selectConditionField>
            <#assign updateColumns = updateColumns + [column]>
        </#if>
    </#list>
    private final ${table.className}Service ${serviceName};

    @Operation(summary = "分页查询")
    @GetMapping("/{size}/{current}")
    public R<PageVo<${table.className}Vo>> getPage(
            @PathVariable("size") @Parameter(description = "页面大小") Integer size
            , @PathVariable("current") @Parameter(description = "当前页面") Integer current
        <#--
            1. = 直接生效
            2. like 必须是字符串才生效
            3. 不是like的其他比较，需要满足限定比较类型
        -->
        <#list updateColumns as updateColumn>
            <#if updateColumn.selectCondition=='='>
                <#call appendPageQueryByCondition(updateColumn)>
            , ${getParameter(updateColumn)}
            <#elseif updateColumn.javaType=='String' && updateColumn.selectCondition=='like'>
            <#assign pageLambdaQuery = pageLambdaQuery + prefixSpace + '.like(StringUtils.hasText('+ updateColumn.javaField +'), '+ table.className +'::get'+ updateColumn.javaField?cap_first +', '+ updateColumn.javaField +')'>
            , ${getParameter(updateColumn)}
            <#elseif comparableType?seq_contains(updateColumn.javaType) && updateColumn.selectCondition!='like'>
                <#call appendPageQueryByCondition(updateColumn)>
            , ${getParameter(updateColumn)}
            </#if>
        </#list>
    ) {
        val page = ${pageLambdaQuery}
                .page(Page.of(current, size));
        return R.ok(PageVo.fromPage(page, ${table.className}Vo.class));
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

    @Operation(summary = "id删除")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "id") Long id) {
        ${serviceName}.removeById(id);
        return R.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping
    public R<Void> removeByIds(@RequestParam("ids") @Parameter(description = "id列表") @Size(min = 1, max = 10) Collection<${primaryColumn.javaType}> ids) {
        ${serviceName}.removeByIds(ids);
        return R.ok();
    }
}