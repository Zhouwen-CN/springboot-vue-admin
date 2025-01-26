package com.yeeiee.security.user;

import com.yeeiee.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * <p>
 * 用户名密码认证实体
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Getter
@Setter
@ToString
public class UserAuthenticationToken extends AbstractAuthenticationToken {
    // 前端传过来的用户名密码
    private String username;
    private String password;

    // 数据库查询用户信息
    private User user;

    public UserAuthenticationToken() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return super.isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        return super.isAuthenticated() ? user : username;
    }
}
