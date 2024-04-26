package com.yeeiee.handler;

import com.yeeiee.utils.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R<Void> defaultHandler(Exception e) {
        return R.error(e.getMessage());
    }
}
