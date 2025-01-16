package com.yeeiee.security.handler;

import com.yeeiee.entity.LoginLog;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StatusEnum;
import com.yeeiee.exception.NamedAuthenticationException;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 登入成功处理
 */

@AllArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private LoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 1.获取登入用户名
        var username = "未知用户";

        if (exception instanceof NamedAuthenticationException) {
            username = ((NamedAuthenticationException) exception).getUsername();
        }

        // 2.写入日志
        val loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setOperation(LoginOperationEnum.LOGIN.getOperation());
        loginLog.setStatus(StatusEnum.FIELD.getStatus());
        loginLog.setIp(CommonUtil.getIpAddr(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLogService.save(loginLog);

        // 3.写出响应
        CommonUtil.writeResponse(response, R.error(HttpStatus.FORBIDDEN, exception.getMessage()));
    }
}
