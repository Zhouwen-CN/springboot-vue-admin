package com.yeeiee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 用户 传输对象
 * </p>
 *
 * @author chen
 * @since 2024/5/8
 */
@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private List<RoleDto> roles;
    private List<MenuDto> menus;
}
