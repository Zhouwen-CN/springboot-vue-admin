package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2025-02-14
 */
@Getter
@Setter
@ToString
@TableName("t_dict_data")
@Schema(name = "DictData", description = "字典数据表")
public class DictData {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "字典类型id")
    @TableField(value = "type_id")
    private Long typeId;

    @Schema(description = "字典键")
    @TableField(value = "label")
    private String label;

    @Schema(description = "字典值")
    @TableField(value = "value")
    private Integer value;

    @Schema(description = "字典排序")
    @TableField(value = "sort")
    private Integer sort;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}