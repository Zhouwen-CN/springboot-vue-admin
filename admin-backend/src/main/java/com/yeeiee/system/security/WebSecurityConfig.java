package com.yeeiee.system.security;

import com.yeeiee.system.security.handler.AuthenticationExceptionHandler;
import com.yeeiee.system.security.handler.AuthorizationExceptionHandler;
import com.yeeiee.system.security.handler.LoginFailureHandler;
import com.yeeiee.system.security.handler.LoginSuccessHandler;
import com.yeeiee.system.security.jwt.JwtAuthenticationFilter;
import com.yeeiee.system.security.refresh.RefreshAuthenticationProcessingFilter;
import com.yeeiee.system.security.user.UserAuthenticationProcessingFilter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

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
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] STATIC_LIST = new String[]{
            "/",
            "/index.html",
            "/favicon.ico",
            "/js/**.js",
            "/css/**.css",
            "/images/**",
            "/assets/**"
    };

    private static final String H2_CONSOLE_PATH = "/h2/**";


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
     *
     * @param http spring security 配置对象
     * @return spring security 过滤器链
     * @throws Exception 抛出异常
     */
    @Bean
    @Order(1)
    public SecurityFilterChain loginApiFilterChain(HttpSecurity http, ProviderManager providerManager) throws Exception {
        this.commonHttpSetting(http);
        // 使用 securityMatcher 限定当前配置作用的路径
        http.securityMatcher("/login/*")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        val pathPatternRequestMatcherBuilder = PathPatternRequestMatcher.withDefaults();

        // 用户名密码登入
        val userAuthenticationFilter = new UserAuthenticationProcessingFilter(
                pathPatternRequestMatcherBuilder.matcher(HttpMethod.POST, "/login/user"),
                providerManager,
                loginSuccessHandler,
                loginFailureHandler
        );
        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 刷新token登入
        val refreshAuthenticationFilter = new RefreshAuthenticationProcessingFilter(
                pathPatternRequestMatcherBuilder.matcher(HttpMethod.GET, "/login/refresh"),
                providerManager,
                loginSuccessHandler,
                loginFailureHandler
        );
        http.addFilterBefore(refreshAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    /**
     * 最后加载的过滤器链
     *
     * @param http spring security 配置对象
     * @return spring security 过滤器链
     * @throws Exception 抛出异常
     */
    @Bean
    public SecurityFilterChain defaultApiFilterChain(HttpSecurity http) throws Exception {
        this.commonHttpSetting(http);
        http.headers(headers -> {
                    // 启用 XSS 过滤。如果检测到攻击，浏览器将不会清除页面，而是阻止页面加载（默认是0）
                    headers.xssProtection(xssProtection -> xssProtection.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));
                    // 默认DENY，h2 控制台会打不开
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                }
        );
        http.authorizeHttpRequests(authorize ->
                        authorize
                                // 静态资源
                                .requestMatchers(HttpMethod.GET, STATIC_LIST).permitAll()
                                // h2 console
                                .requestMatchers(H2_CONSOLE_PATH).permitAll()
                                // actuator 端点
                                .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class)).hasAuthority("admin")
                                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                                // 响应式SSE接口特殊，需要手动鉴权
                                .requestMatchers(HttpMethod.POST,"/ai/chat").permitAll()
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

    /**
     * provider 管理者
     *
     * @param authenticationProviders              认证提供者列表
     * @param defaultAuthenticationEventPublishers 默认事件发布
     * @return provider manager
     */
    @Bean
    public ProviderManager providerManager(
            ObjectProvider<AuthenticationProvider> authenticationProviders,
            ObjectProvider<DefaultAuthenticationEventPublisher> defaultAuthenticationEventPublishers
    ) {
        val providerList = authenticationProviders.orderedStream().toList();
        val providerManager = new ProviderManager(providerList);

        defaultAuthenticationEventPublishers.ifUnique(authenticationEventPublisher -> {
            authenticationEventPublisher.setDefaultAuthenticationFailureEvent(AuthenticationFailureBadCredentialsEvent.class);
            providerManager.setAuthenticationEventPublisher(authenticationEventPublisher);
        });

        return providerManager;
    }
}