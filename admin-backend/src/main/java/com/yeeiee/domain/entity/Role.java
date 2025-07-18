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
 * 角色表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Getter
@Setter
@ToString
@TableName("t_role")
@Schema(name = "Role", description = "角色表")
public class Role {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色名称")
    @TableField(value = "role_name",keepGlobalFormat = true)
    private String roleName;

    @Schema(description = "角色说明")
    @TableField(value = "desc",keepGlobalFormat = true)
    private String desc;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT,keepGlobalFormat = true)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE,keepGlobalFormat = true)
    private LocalDateTime updateTime;
}
