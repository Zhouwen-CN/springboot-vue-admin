package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
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
 * @since 2025-09-15
 */
@Getter
@Setter
@ToString
@TableName("t_dict_data")
@KeySequence("t_dict_data_seq")
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
    @TableField(value = "data")
    private Integer data;

    @Schema(description = "字典排序")
    @TableField(value = "sort_id")
    private Integer sortId;

    @Schema(description = "创建者")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}