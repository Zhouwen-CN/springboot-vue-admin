package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户信息，包含角色列表，查询用户分页
 * </p>
 *
 * @author chen
 * @since 2024-05-13
 */
@Getter
@Setter
@ToString
public class UserRoleVo {
    private Long id;
    private String username;
    private String password;
    private List<RoleVo> roleList;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
