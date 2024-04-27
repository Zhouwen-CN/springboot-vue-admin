package com.yeeiee.handler;

import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.utils.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(DmlOperationException.class)
    public R<Void> dmlFailureHandler(DmlOperationException e) {
        return R.error(HttpStatus.NOT_MODIFIED, e);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> defaultHandler(Exception e) {
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
