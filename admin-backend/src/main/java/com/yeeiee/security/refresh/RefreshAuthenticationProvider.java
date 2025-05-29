package com.yeeiee.security.refresh;

import com.yeeiee.domain.entity.User;
import com.yeeiee.exception.VerifyTokenException;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 刷新 token 认证提供者
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Resolve the token and verify the token version");
        val refreshToken = (String) authentication.getCredentials();

        val optional = jwtUtil.parseRefreshToken(refreshToken);
        if (optional.isEmpty()) {
            throw new VerifyTokenException("token解析失败");
        }

        val claimMap = optional.get();
        val username = claimMap.get("username").asString();
        val version = claimMap.get("version").asLong();

        val user = userService.lambdaQuery()
                .eq(User::getUsername, username)
                .one();

        // 检查 token version
        if (user == null || version != user.getTokenVersion() - 1) {
            throw new VerifyTokenException("token校验失败");
        }

        val token = new RefreshAuthenticationToken();
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        token.setAuthenticated(true);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RefreshAuthenticationToken.class);
    }
}
