package com.yeeiee.system.security.handler;

import com.yeeiee.ai.controller.ChatController;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

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
        val chatId = request.getHeader(ChatController.CHAT_HEADER);
        val error = R.error(HttpStatus.UNAUTHORIZED, "用户未认证");
        if (StringUtils.hasText(chatId)) {
            ResponseObjectUtil.writeStream(response, error);
        } else {
            ResponseObjectUtil.writeJson(response, error);
        }
    }
}
