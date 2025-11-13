package com.yeeiee.system.domain.entity;

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
 * 角色菜单关系表
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
@Getter
@Setter
@ToString
@TableName("t_role_menu")
@KeySequence("t_role_menu_seq")
@Schema(name = "RoleMenu", description = "角色菜单关系表")
public class RoleMenu {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "角色id")
    @TableField(value = "role_id")
    private Long roleId;

    @Schema(description = "菜单id")
    @TableField(value = "menu_id")
    private Long menuId;

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