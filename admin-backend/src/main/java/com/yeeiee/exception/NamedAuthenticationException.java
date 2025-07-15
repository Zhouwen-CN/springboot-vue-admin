package com.yeeiee.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * 认证失败异常，需要传递认证失败的用户名
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Getter
public class NamedAuthenticationException extends AuthenticationException {

    private String username;

    public NamedAuthenticationException(String msg){
        super(msg);
    }

    public NamedAuthenticationException(String username, String msg) {
        super(msg);
        this.username = username;
    }
}
