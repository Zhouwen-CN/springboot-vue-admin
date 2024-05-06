package com.yeeiee.security;

import com.yeeiee.utils.R;
import com.yeeiee.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

/**
 * <p>
 * spring security 配置类
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private JwtUserDetailService jwtUserDetailService;

    private JwtAuthenticationFilter jwtAuthFilter;

    private static final String[] WHITE_LIST = new String[]{
            "/login",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(WHITE_LIST)
                        // permitAll 表示不进行拦截
                        .permitAll()
                        // 访问某些资源需要某些权限，这是使用更为简单的ACL，而不是RBAC
                        .requestMatchers("/field/**").hasAuthority("field")
                        .requestMatchers("/range/**").hasAuthority("range")
                        .requestMatchers("/storey/**").hasAuthority("storey")
                        .requestMatchers("/word/**").hasAuthority("word")
                        // 对所有的请求开启权限保护
                        .anyRequest()
                        // 已认证的请求会被自动授权
                        .authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                            // 未认证处理
                            exception.authenticationEntryPoint((HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                AuthenticationException authException
                                    ) -> ResponseUtil.writeResponse(response, R.error(HttpStatus.UNAUTHORIZED, authException))
                            );
                            // 未授权处理
                            exception.accessDeniedHandler((HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           AccessDeniedException accessDeniedException
                            ) -> ResponseUtil.writeResponse(response, R.error(HttpStatus.UNAUTHORIZED, accessDeniedException)));
                        }
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(jwtUserDetailService);
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}