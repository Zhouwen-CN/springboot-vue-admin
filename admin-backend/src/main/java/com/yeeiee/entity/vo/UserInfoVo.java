package com.yeeiee.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 用户信息，包含 角色列表 和 菜单列表
 * </p>
 *
 * @author chen
 * @since 2024/5/13
 */
@Getter
@Setter
@ToString
public class UserInfoVo {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    // token 版本
    @JsonIgnore
    private Long tokenVersion;
    private String accessToken;
    private String refreshToken;
    private List<RoleVo> roleList;
}
