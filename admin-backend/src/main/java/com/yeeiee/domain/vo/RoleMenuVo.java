package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色菜单视图对象
 * </p>
 *
 * @author chen
 * @since 2025-06-10
 */
@Getter
@Setter
@ToString
@Schema(name = "RoleMenuVo", description = "角色菜单视图对象")
public class RoleMenuVo {
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
    @Schema(description = "菜单id列表")
    private List<Long> menuIds;
}
