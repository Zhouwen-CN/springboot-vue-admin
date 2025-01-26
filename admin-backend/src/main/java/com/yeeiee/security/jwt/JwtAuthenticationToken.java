package com.yeeiee.security.jwt;

import com.yeeiee.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>
 * jwt认证实体
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Getter
@Setter
@ToString
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String accessToken;
    private User user;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return super.isAuthenticated() ? null : accessToken;
    }

    @Override
    public Object getPrincipal() {
        return super.isAuthenticated() ? user : accessToken;
    }
}
