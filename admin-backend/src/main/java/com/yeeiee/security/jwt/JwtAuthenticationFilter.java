package com.yeeiee.security.jwt;

import com.yeeiee.entity.User;
import com.yeeiee.service.RoleService;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserService userService;

    private RoleService roleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Try to get and parse the token");
        // 从 request 获取 JWT token
        val token = JwtTokenUtil.getTokenFromRequest(request);

        // 校验 token
        val optional = JwtTokenUtil.parseAccessToken(token);
        if (optional.isPresent()) {
            val claimMap = optional.get();

            val username = claimMap.get("username").asString();
            val version = claimMap.get("version").asLong();


            // 加载与 token 关联的用户
            val user = userService.lambdaQuery()
                    .eq(User::getUsername, username)
                    .one();

            // 检查 token version
            if (version == user.getTokenVersion() - 1) {
                val authorities = claimMap.get("roleNames")
                        .asList(String.class)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                val authenticated = new JwtAuthenticationToken(authorities);
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
