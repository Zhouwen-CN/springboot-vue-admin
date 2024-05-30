package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息，包含角色列表
 * </p>
 *
 * @author chen
 * @since 2024/5/13
 */
@Getter
@Setter
@ToString
public class UserRoleVo {
    private Long id;
    private String username;
    private String password;
    private String roleIds;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
