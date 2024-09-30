package com.yeeiee.security;

import com.yeeiee.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * jwt 过滤器
 * </p>
 *
 * @author chen
 * @since 2024-05-06
 */
@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUserDetailServiceImpl jwtUserDetailServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 从 request 获取 JWT token
        val token = JwtTokenUtil.getTokenFromRequest(request);

        val optional = JwtTokenUtil.parseAccessToken(token);
        // 校验 token
        if (optional.isPresent()) {
            // 从 token 获取 username 和 access_token_version
            val claimMap = optional.get();
            val username = claimMap.get("username").asString();
            val version = claimMap.get("version").asLong();
            // 加载与 token 关联的用户
            val securityUser = (SecurityUser) jwtUserDetailServiceImpl.loadUserByUsername(username);
            if (version == securityUser.getTokenVersion() - 1) {
                // 将用户信息存入 authentication
                val authenticated = UsernamePasswordAuthenticationToken.authenticated(
                        securityUser,
                        null,
                        securityUser.getAuthorities());
                // 一些附加信息（远程主机地址，sessionId）
                authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将 authentication 存入 ThreadLocal
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                log.info("authenticated user {}, setting security context", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
