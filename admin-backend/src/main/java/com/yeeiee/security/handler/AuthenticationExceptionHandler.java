package com.yeeiee.security.handler;

import com.yeeiee.utils.R;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * <p>
 * 未认证异常处理
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseObjectUtil.writeResponse(response, R.error(HttpStatus.UNAUTHORIZED, "用户未认证"));
    }
}
