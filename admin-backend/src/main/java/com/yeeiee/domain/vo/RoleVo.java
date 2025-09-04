package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色视图对象
 * </p>
 *
 * @author chen
 * @since 2025-06-10
 */
@Getter
@Setter
@ToString
@Schema(name = "RoleVo", description = "角色菜单视图对象")
public class RoleVo {
    @Schema(description = "角色id")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色描述")
    private String description;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
