package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 用户信息，包含角色id列表 和 菜单列表
 * </p>
 *
 * @author chen
 * @since 2024/5/13
 */
@Getter
@Setter
@ToString
public class UserInfoVo {
    private String username;
    private String token;
    private List<Long> roleIds;
}
