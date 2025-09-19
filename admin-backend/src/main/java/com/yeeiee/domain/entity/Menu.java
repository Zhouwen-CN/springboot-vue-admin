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
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2025-09-19
 */
@Getter
@Setter
@ToString
@TableName("t_menu")
@KeySequence("t_menu_seq")
@Schema(name = "Menu", description = "菜单表")
public class Menu {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "标题")
    @TableField(value = "title")
    private String title;

    @Schema(description = "访问路径")
    @TableField(value = "access_path")
    private String accessPath;

    @Schema(description = "图标")
    @TableField(value = "icon")
    private String icon;

    @Schema(description = "父级菜单id")
    @TableField(value = "pid")
    private Long pid;

    @Schema(description = "是否缓存")
    @TableField(value = "keep_alive")
    private Boolean keepAlive;

    @Schema(description = "菜单类型：0-目录，1-菜单")
    @TableField(value = "menu_type")
    private Integer menuType;

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