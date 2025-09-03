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
 * 用户角色关系表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Getter
@Setter
@ToString
@TableName("t_user_role")
@Schema(name = "UserRole", description = "用户角色关系表")
public class UserRole {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "用户id")
    @TableField(value = "user_id", keepGlobalFormat = true)
    private Long userId;

    @Schema(description = "角色id")
    @TableField(value = "role_id", keepGlobalFormat = true)
    private Long roleId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT, keepGlobalFormat = true)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE, keepGlobalFormat = true)
    private LocalDateTime updateTime;
}
