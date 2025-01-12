package com.yeeiee.security.user;

import com.yeeiee.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 所有的 token，登入成功状态 Principal 需要始终返回 User 对象
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
