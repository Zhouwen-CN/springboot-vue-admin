package com.yeeiee.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * <p>
 * 登入操作枚举类
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Getter
public enum LoginOperationEnum implements IEnum<Integer> {
    LOGIN(1, "登入"),
    LOGOUT(0, "退出"),
    @JsonEnumDefaultValue
    UNKNOWN(-1, "未知操作");

    private final String operation;
    private final Integer code;

    LoginOperationEnum(int code, String operation) {
        this.operation = operation;
        this.code = code;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.operation;
    }
}
