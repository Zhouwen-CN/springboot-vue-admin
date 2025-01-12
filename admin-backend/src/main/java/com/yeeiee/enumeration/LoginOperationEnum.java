package com.yeeiee.enumeration;

import lombok.Getter;

@Getter
public enum LoginOperationEnum {
    LOGIN("登入"),
    LOGOUT("退出");

    private final String operation;

    LoginOperationEnum(String operation) {
        this.operation = operation;
    }
}
