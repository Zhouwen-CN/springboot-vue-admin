<#--@formatter:off-->
package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
* <p>
* ${table.tableComment?remove_ending('表')}视图
* </p>
*
* @author ${table.author}
* @since ${.now?string('yyyy-MM-dd')}
*/
@Getter
@Setter
@ToString
@Schema(name = "${table.className}Vo", description = "${table.tableComment?remove_ending('表')}视图")
public class ${table.className}Vo {

<#list columns as column>
<#if column.selectResultField>
    @Schema(description = "${column.columnComment}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}