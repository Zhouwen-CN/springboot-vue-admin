package com.yeeiee.enumeration;

public enum LoginOperationEnum {
    LOGIN(1, "登入"),
    LOGOUT(0, "退出");

    private final String operation;
    private final Integer code;

    LoginOperationEnum(int code, String operation) {
        this.operation = operation;
        this.code = code;
    }

    public static String getOperation(Integer code) {
        for (LoginOperationEnum value : values()) {
            if (value.code.equals(code)) {
                return value.operation;
            }
        }

        return "未知操作";
    }

    public String getOperation() {
        return this.operation;
    }
}
