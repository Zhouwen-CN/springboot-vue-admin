package com.yeeiee.common.exception;

import lombok.Getter;
import lombok.experimental.StandardException;

/**
 * <p>
 * 响应状态码异常
 * </p>
 *
 * @author chen
 * @since 2025-08-20
 */
@StandardException
@Getter
public class HttpStatusException extends RuntimeException{

    private int httpStatus;

    public HttpStatusException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
