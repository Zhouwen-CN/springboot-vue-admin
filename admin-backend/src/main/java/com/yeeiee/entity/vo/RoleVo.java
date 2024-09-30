package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 角色信息，用作用户信息的属性
 * </p>
 *
 * @author chen
 * @since 2024/9/30
 */
@Getter
@Setter
@ToString
public class RoleVo {
    private Long id;
    private String roleName;
}
