package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户角色视图对象
 * </p>
 *
 * @author chen
 * @since 2024-05-13
 */
@Getter
@Setter
@ToString
@Schema(name = "UserRoleVo", description = "用户角色视图对象")
public class UserRoleVo {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "用户角色列表")
    private List<RoleVo> roleList;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
