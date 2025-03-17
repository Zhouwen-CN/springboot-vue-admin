package com.yeeiee.security;

import com.yeeiee.security.handler.AuthenticationExceptionHandler;
import com.yeeiee.security.handler.AuthorizationExceptionHandler;
import com.yeeiee.security.handler.LoginFailureHandler;
import com.yeeiee.security.handler.LoginSuccessHandler;
import com.yeeiee.security.jwt.JwtAuthenticationFilter;
import com.yeeiee.security.user.UserAuthenticationProcessingFilter;
import com.yeeiee.security.user.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * <p>
 * spring security 配置类
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] WHITE_LIST = new String[]{
            "/",
            "/index.html",
            "/favicon.ico",
            "/js/**.js",
            "/css/**.css",
            "/images/**",
            "/assets/**",
            // swagger-ui，静态资源
            // "/swagger-ui.html",
            // "/v3/api-docs",
            // "/v3/api-docs/swagger-config",
            // "/swagger-ui/**"
    };

    private void commonHttpSetting(HttpSecurity http) throws Exception {
        // 关闭一些不需要的
        http.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);


        http.exceptionHandling(exception -> {
                    // 未认证处理
                    exception.authenticationEntryPoint(new AuthenticationExceptionHandler())
                            // 未授权处理
                            .accessDeniedHandler(new AuthorizationExceptionHandler());
                }
        );
    }

    /**
     * 登入 api，可以添加多种登入方式，方便扩展
     * @param http spring security 配置对象
     * @return spring security 过滤器链
     * @throws Exception 抛出异常
     */
    @Bean
    @Order(1)
    public SecurityFilterChain loginApiFilterChain(HttpSecurity http) throws Exception {
        this.commonHttpSetting(http);
        // 使用 securityMatcher 限定当前配置作用的路径
        http.securityMatcher("/login/*")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        // 用户名密码登入
        val userAuthenticationFilter = new UserAuthenticationProcessingFilter(
                new AntPathRequestMatcher("/login/user", HttpMethod.POST.name()),
                userAuthenticationProvider,
                loginSuccessHandler,
                loginFailureHandler
        );
        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 最后加载的过滤器链
     * @param http spring security 配置对象
     * @return spring security 过滤器链
     * @throws Exception 抛出异常
     */
    @Bean
    public SecurityFilterChain defaultApiFilterChain(HttpSecurity http) throws Exception {
        this.commonHttpSetting(http);
        http.authorizeHttpRequests(authorize ->
                        authorize
                                // 静态资源和swagger
                                .requestMatchers(HttpMethod.GET, WHITE_LIST).permitAll()
                                // 刷新 token
                                .requestMatchers(HttpMethod.GET, "/user/refresh").permitAll()
                                // 获取用户所属的菜单列表
                                .requestMatchers(HttpMethod.GET, "/menu", "/user/logout/**").authenticated()
                                // 只有 admin 角色才能访问权限管理
                                .requestMatchers("/user/**", "/role/**", "/menu/**").hasAuthority("admin")
                                // 对所有的请求开启权限保护
                                .anyRequest()
                                // 已认证的请求会被自动授权
                                .authenticated()
                )
                // 在账号密码认证之前，先进行jwt校验
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}