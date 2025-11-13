package com.yeeiee.system.security.handler;

import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.exception.NamedAuthenticationException;
import com.yeeiee.system.domain.entity.LoginLog;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.service.LoginLogService;
import com.yeeiee.utils.IPUtil;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 登入成功处理
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final LoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 获取登入用户名
        var username = "未知用户";

        if (exception instanceof NamedAuthenticationException namedAuthenticationException) {
            val exceptionUsername = namedAuthenticationException.getUsername();
            if (exceptionUsername != null) username = exceptionUsername;
        }

        // 写入日志
        val loginLog = new LoginLog();
        loginLog.setOperation(LoginOperationEnum.LOGIN);
        loginLog.setStatus(OperationStatusEnum.FAILURE);
        loginLog.setIp(IPUtil.getClientIP(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLog.setCreateUser(username);
        loginLogService.save(loginLog);

        // 写出响应
        ResponseObjectUtil.writeResponse(response, R.error(HttpStatus.FORBIDDEN, exception.getMessage()));
    }
}
