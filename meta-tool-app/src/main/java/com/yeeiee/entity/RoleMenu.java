package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户权限关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Getter
@Setter
@ToString
@TableName("`t_role_menu`")
@Schema(name = "RoleMenu", description = "用户权限关系表")
public class RoleMenu {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色id")
    @TableField("`role_id`")
    private Long roleId;

    @Schema(description = "菜单id")
    @TableField("`menu_id`")
    private Long menuId;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
