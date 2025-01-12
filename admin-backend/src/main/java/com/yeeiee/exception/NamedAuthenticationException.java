package com.yeeiee.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


/**
 * 需要传递认证失败的用户名
 */
@Getter
public class NamedAuthenticationException extends AuthenticationException {

    private final String username;

    public NamedAuthenticationException(String username, String msg) {
        super(msg);

        this.username = username;
    }
}
