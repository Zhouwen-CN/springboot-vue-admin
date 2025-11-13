package com.yeeiee.system.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2025-10-22
 */
@Getter
@Setter
@ToString
@TableName("t_dict_type")
@KeySequence("t_dict_type_seq")
public class DictType {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 字典名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 字典是否启用：0-禁用，1-启用
     */
    @TableField(value = "dict_enable")
    private Boolean dictEnable;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}