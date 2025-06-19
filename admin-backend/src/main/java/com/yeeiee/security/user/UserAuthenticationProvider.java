package com.yeeiee.security.user;

import com.yeeiee.cache.UserCacheManager;
import com.yeeiee.exception.NamedAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户名密码认证提供者
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserCacheManager userCacheManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Look up the data and verify whether the account password is correct");
        val username = (String) authentication.getPrincipal();
        val password = (String) authentication.getCredentials();

        // 这里不需要缓存，缓存登入成功也会被清除
        val user = userCacheManager.getUserByUsername(username,false);

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new NamedAuthenticationException(username, "用户名或密码错误");
        }

        val token = new UserAuthenticationToken();
        token.setUser(user);
        token.setAuthenticated(true);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserAuthenticationToken.class);
    }
}
