package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2025-02-13
 */
@Getter
@Setter
@ToString
@TableName("t_dict_type")
@Schema(name = "DictType", description = "字典类型表")
public class DictType {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典名称")
    @TableField(value = "name",keepGlobalFormat = true)
    private String name;

    @Schema(description = "字典类型")
    @TableField(value = "type",keepGlobalFormat = true)
    private String type;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT,keepGlobalFormat = true)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE,keepGlobalFormat = true)
    private LocalDateTime updateTime;
}