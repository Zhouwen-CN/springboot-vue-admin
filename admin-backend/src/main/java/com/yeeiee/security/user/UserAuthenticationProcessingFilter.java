package com.yeeiee.security.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.exception.NamedAuthenticationException;
import com.yeeiee.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户名密码认证过滤器
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Slf4j
public class UserAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public UserAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                              AuthenticationProvider authenticationProvider,
                                              AuthenticationSuccessHandler authenticationSuccessHandler,
                                              AuthenticationFailureHandler authenticationFailureHandler) {
        super(requiresAuthenticationRequestMatcher, new ProviderManager(authenticationProvider));
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("Obtain the account password from the login form");

        val body = request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        LoginDto loginDto;
        try {
            loginDto = JsonUtil.parse(body, LoginDto.class);
        } catch (JsonProcessingException e) {
            throw new NamedAuthenticationException("未知用户", "请求参数解析失败");
        }

        val unauthenticated = new UserAuthenticationToken();
        unauthenticated.setUsername(loginDto.getUsername());
        unauthenticated.setPassword(loginDto.getPassword());
        unauthenticated.setAuthenticated(false);

        return super.getAuthenticationManager().authenticate(unauthenticated);
    }
}
