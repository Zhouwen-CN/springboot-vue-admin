package com.yeeiee.system.security.jwt;

import com.yeeiee.system.domain.entity.User;
import com.yeeiee.system.security.JwtTokenProvider;
import com.yeeiee.system.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
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
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Try to get and parse the access token");
        // 从 request 获取 JWT token
        val token = request.getHeader(HttpHeaders.AUTHORIZATION);

        val claimsOptional = jwtTokenProvider.getClaims(token);
        if (claimsOptional.isPresent()) {
            val claims = claimsOptional.get();
            val userId = Long.parseLong(claims.getSubject());

            // 加载与 token 关联的用户
            User user = userService.getUserByUserId(userId);

            // 检查用户和刷新token
            if (user != null && user.getRefreshToken() != null) {
                val authorities = jwtTokenProvider.getRoleNames(claims)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                val authenticated = new JwtAuthenticationToken(authorities);
                authenticated.setToken(token);
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
