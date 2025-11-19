package com.yeeiee.system.security.handler;

import com.yeeiee.ai.controller.ChatController;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

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
        val chatId = request.getHeader(ChatController.CHAT_HEADER);
        val error = R.error(HttpStatus.FORBIDDEN, "用户未授权");
        if (StringUtils.hasText(chatId)) {
            ResponseObjectUtil.writeStream(response, error);
        } else {
            ResponseObjectUtil.writeJson(response, error);
        }
    }
}
