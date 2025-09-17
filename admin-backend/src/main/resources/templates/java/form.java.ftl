<#--@formatter:off-->
package ${package.form};

import ${package.base}.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * <p>
 * ${table.tableComment?remove_ending('表')}表单
 * </p>
 *
 * @author ${table.author}
 * @since ${.now?string('yyyy-MM-dd')}
 */
@Getter
@Setter
@ToString
@Schema(name = "${table.className}Form", description = "${table.tableComment?remove_ending('表')}表单")
public class ${table.className}Form {

<#list columns as column>
<#if column.insertField || column.updateField>
    <#if column.insertField && column.updateField>
        <#assign group = "(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})"/>
    <#elseif column.insertField>
        <#assign group = "(groups = {GroupingValidate.Create.class})"/>
    <#elseif column.updateField>
        <#assign group = "(groups = {GroupingValidate.Update.class})"/>
    </#if>
    <#if column.columnLength??>
    @NotBlank${group}
    @Length${group?remove_ending(")")}, max = ${column.columnLength})
    <#else>
    @NotNull${group}
    </#if>
    @Schema(description = "${column.columnComment}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}