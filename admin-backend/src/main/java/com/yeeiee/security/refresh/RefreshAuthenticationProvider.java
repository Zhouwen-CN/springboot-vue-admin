package com.yeeiee.security.refresh;

import com.yeeiee.domain.entity.User;
import com.yeeiee.exception.NamedAuthenticationException;
import com.yeeiee.exception.VerifyTokenException;
import com.yeeiee.security.JwtTokenProvider;
import com.yeeiee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Resolve the token and verify the token version");
        val refreshToken = (String) authentication.getCredentials();

        val payload = jwtTokenProvider.parseRefreshToken(refreshToken)
                .orElseThrow(() -> new VerifyTokenException("token解析失败"))
                .getPayload();

        val username = payload.getSubject();
        val version = payload.get("version", Long.class);

        // 加载与 token 关联的用户（认证异常以外的异常需要手动处理，不然会抛到控制台）
        User user;
        try {
            user = userService.getUserByUsername(username);
        } catch (Exception e) {
            val rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
            log.error("Refresh authentication provider exception by: " + rootCauseMessage);
            throw new NamedAuthenticationException(username, rootCauseMessage);
        }

        // 检查 token version
        if (user == null || version != user.getTokenVersion() - 1) {
            throw new VerifyTokenException("token校验失败");
        }

        val token = new RefreshAuthenticationToken();
        token.setUser(user);
        token.setAuthenticated(true);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RefreshAuthenticationToken.class);
    }
}
