package com.yeeiee.security.jwt;

import com.yeeiee.cache.UserCacheManager;
import com.yeeiee.security.JwtTokenProvider;
import com.yeeiee.utils.RequestObjectUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * jwt认证过滤器
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserCacheManager userCacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Try to get and parse the access token");
        // 从 request 获取 JWT token
        val token = RequestObjectUtil.getTokenFromRequest(request);

        // 校验 token
        val optional = jwtTokenProvider.parseAccessToken(token);
        if (optional.isPresent()) {
            val payload = optional.get().getPayload();

            val username = payload.getSubject();
            val version = payload.get("version", Long.class);

            // 加载与 token 关联的用户
            val user = userCacheManager.getUserByUsername(username,true);

            // 检查 token version
            if (user != null && version == user.getTokenVersion() - 1) {
                val authorities = jwtTokenProvider.getRoles(payload)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                val authenticated = new JwtAuthenticationToken(authorities);
                authenticated.setAccessToken(token);
                authenticated.setUser(user);
                authenticated.setAuthenticated(true);

                // 将 authentication 存入 ThreadLocal
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                log.debug("The verification token is successful");
            }
        }

        filterChain.doFilter(request, response);
    }
}
