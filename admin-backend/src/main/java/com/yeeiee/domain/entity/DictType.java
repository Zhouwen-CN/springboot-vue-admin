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
 * 字典类型表
 * </p>
 *
 * @author chen
 * @since 2025-09-04
 */
@Getter
@Setter
@ToString
@TableName("t_dict_type")
@KeySequence("t_dict_type_seq")
@Schema(name = "DictType", description = "字典类型表")
public class DictType {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "字典类型")
    @TableField(value = "dict_type")
    private String dictType;

    @Schema(description = "字典名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}