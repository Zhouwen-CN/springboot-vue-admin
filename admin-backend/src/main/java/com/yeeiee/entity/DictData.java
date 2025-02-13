package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
@Getter
@Setter
@ToString
@TableName("`t_dict_data`")
@Schema(name = "DictData", description = "字典数据表")
public class DictData {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典类型id")
    @TableField("`type_id`")
    private Long typeId;

    @Schema(description = "字典键")
    @TableField("`dict_key`")
    private String dictKey;

    @Schema(description = "字典值")
    @TableField("`dict_value`")
    private String dictValue;

    @Schema(description = "字典排序")
    @TableField("`dict_sort`")
    private Integer dictSort;

    @Schema(description = "是否启用")
    @TableField("`is_enable`")
    private Boolean enable;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}