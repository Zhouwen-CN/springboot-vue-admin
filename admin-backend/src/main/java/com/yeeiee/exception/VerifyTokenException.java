package com.yeeiee.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * 检验token失败异常
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */

public class VerifyTokenException extends AuthenticationException {

    public VerifyTokenException(String msg) {
        super(msg);
    }
}
