package com.yeeiee.security.handler;

import com.yeeiee.utils.R;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * <p>
 * 未授权异常处理
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
public class AuthorizationExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseObjectUtil.writeResponse(response, R.error(HttpStatus.FORBIDDEN, "用户未授权"));
    }
}
