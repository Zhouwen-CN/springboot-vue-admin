package com.yeeiee.security.refresh;

import com.yeeiee.domain.entity.User;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * <p>
 * 刷新 token 认证实体
 * </p>
 *
 * @author chen
 * @since 2025-05-29
 */

@Setter
public class RefreshAuthenticationToken extends AbstractAuthenticationToken {
    private String refreshToken;
    private User user;

    public RefreshAuthenticationToken() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return super.isAuthenticated() ? null : refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return super.isAuthenticated() ? user : refreshToken;
    }
}
