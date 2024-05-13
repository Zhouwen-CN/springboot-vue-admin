package com.yeeiee.entity.dto;

import com.yeeiee.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 查询用户信息和用户角色信息
 * </p>
 *
 * @author chen
 * @since 2024/5/8
 */
@Getter
@Setter
@ToString
public class UserRoleDto {
    private Long id;
    private String username;
    private String password;
    private List<Role> roles;
}
