package com.yeeiee.exception;

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
    public R<String> badCredentialsHandler() {
        return R.error(HttpStatus.FORBIDDEN, "用户名或密码错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public R<String> authenticationExceptionHandler(AuthenticationException e) {
        return R.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }


    @ExceptionHandler(DmlOperationException.class)
    public R<String> dmlFailureHandler(DmlOperationException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<String> noResourceFoundHandler(NoResourceFoundException e) {
        return R.error(HttpStatus.NOT_FOUND, e);
    }

//    @ExceptionHandler(Exception.class)
//    public R<Void> defaultHandler(Exception e) {
//        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
//    }
}
