<#--@formatter:off-->
package ${package.entity};

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.KeySequence;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * ${table.tableComment}
 * </p>
 *
 * @author ${table.author}
 * @since ${.now?string('yyyy-MM-dd')}
 */
@Getter
@Setter
@ToString
@TableName("${table.tableName}")
@KeySequence("${table.tableName}_SEQ")
public class ${table.className} {

<#list columns as column>
    /**
     * ${column.columnComment}
     */
    <#if column.primaryKey>
    @TableId(value = "${column.columnName}")
    <#elseif column.columnName?lower_case='create_user' || column.columnName?lower_case='create_time'>
    @TableField(value = "${column.columnName}", fill = FieldFill.INSERT)
    <#elseif column.columnName?lower_case='update_user' || column.columnName?lower_case='update_time'>
    @TableField(value = "${column.columnName}", fill = FieldFill.INSERT_UPDATE)
    <#else>
    @TableField(value = "${column.columnName}")
    </#if>
    private ${column.javaType} ${column.javaField};

</#list>
}