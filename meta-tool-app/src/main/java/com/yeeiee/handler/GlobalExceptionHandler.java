package com.yeeiee.handler;

import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.utils.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public R<Void> badCredentialsHandler() {
        return R.error(HttpStatus.FORBIDDEN, "用户名或密码错误");
    }

    @ExceptionHandler(DmlOperationException.class)
    public R<Void> dmlFailureHandler(DmlOperationException e) {
        return R.error(HttpStatus.NO_CONTENT, e);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<Void> noResourceFoundHandler(NoResourceFoundException e) {
        return R.error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> defaultHandler(Exception e) {
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
