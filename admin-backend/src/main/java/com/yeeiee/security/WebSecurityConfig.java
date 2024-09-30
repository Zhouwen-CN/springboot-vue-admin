package com.yeeiee.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeeiee.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * <p>
 * spring security 配置类
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private static final String[] WHITE_LIST = new String[]{
            "/",
            "/index.html",
            "/favicon.ico",
            "/assets/**.js",
            "/assets/**.css",
            "/assets/**.jpg",
            "/assets/**.png",
            "/assets/**.svg",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    private JwtUserDetailServiceImpl jwtUserDetailServiceImpl;
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                // 静态资源和swagger
                                .requestMatchers(HttpMethod.GET, WHITE_LIST).permitAll()
                                // 用户登入
                                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                                // 获取用户所属的菜单列表
                                .requestMatchers(HttpMethod.GET, "/menu").authenticated()
                                // 只有 admin 角色才能访问权限管理
                                .requestMatchers("/user/**", "/role/**", "/menu/**").hasRole("admin")
                                // 对所有的请求开启权限保护
                                .anyRequest()
                                // 已认证的请求会被自动授权
                                .authenticated()
                )
                // 在账号密码认证之前，先进行jwt校验
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                            // 未认证处理
                            exception.authenticationEntryPoint((HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                AuthenticationException authException
                                    ) -> writeResponse(response, R.error(HttpStatus.UNAUTHORIZED, "用户未认证"))
                                    // 未授权处理
                            ).accessDeniedHandler((HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   AccessDeniedException accessDeniedException
                            ) -> writeResponse(response, R.error(HttpStatus.FORBIDDEN, "用户未授权")));
                        }
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        val provider = new DaoAuthenticationProvider(bCryptPasswordEncoder());
        provider.setUserDetailsService(jwtUserDetailServiceImpl);
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private <T> void writeResponse(HttpServletResponse response, R<T> result) throws IOException {
        val objectMapper = new ObjectMapper();
        val resultAsJson = objectMapper.writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(resultAsJson);
    }
}