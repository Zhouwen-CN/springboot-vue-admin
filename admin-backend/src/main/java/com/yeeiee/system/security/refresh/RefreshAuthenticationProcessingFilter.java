package com.yeeiee.system.security.refresh;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

/**
 * <p>
 * 刷新 token 认证过滤器
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Slf4j
public class RefreshAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public RefreshAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                                 ProviderManager providerManager,
                                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                                 AuthenticationFailureHandler authenticationFailureHandler) {
        super(requiresAuthenticationRequestMatcher, providerManager);
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("Obtain the refreshToken from the request header");
        val refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        val refreshAuthenticationToken = new RefreshAuthenticationToken();
        refreshAuthenticationToken.setRefreshToken(refreshToken);
        refreshAuthenticationToken.setAuthenticated(false);

        return super.getAuthenticationManager().authenticate(refreshAuthenticationToken);
    }
}
