package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@Getter
@Setter
@ToString
@TableName("t_menu")
@KeySequence("t_menu_seq")
public class Menu {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 访问路径
     */
    @TableField(value = "access_path")
    private String accessPath;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 父级菜单id
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 是否缓存
     */
    @TableField(value = "keep_alive")
    private Boolean keepAlive;

    /**
     * 菜单类型：0-目录，1-菜单
     */
    @TableField(value = "menu_type")
    private Integer menuType;

    /**
     * 菜单排序
     */
    @TableField(value = "sort_id")
    private Integer sortId;

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