package com.yeeiee.security;

import com.yeeiee.entity.vo.RoleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * spring security 用户实体类
 * </p>
 *
 * @author chen
 * @since 2024/9/30
 */
@Builder
@ToString
public class SecurityUser implements UserDetails, CredentialsContainer {
    private final @Getter Long id;
    private final String username;
    private final @Getter Long version;
    private final @Getter List<RoleVo> roleList;
    private String password;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream().map(roleVo -> new SimpleGrantedAuthority("ROLE_" + roleVo.getRoleName())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
