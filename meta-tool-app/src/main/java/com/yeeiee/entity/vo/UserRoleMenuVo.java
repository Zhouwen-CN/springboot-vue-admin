package com.yeeiee.entity.vo;

import com.yeeiee.entity.Menu;
import com.yeeiee.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 用户角色列表
 * </p>
 *
 * @author chen
 * @since 2024/5/13
 */
@Getter
@Setter
@ToString
public class UserRoleMenuVo {
    private Long id;
    private String username;
    private List<Role> roles;
    private List<Menu> menus;
}
