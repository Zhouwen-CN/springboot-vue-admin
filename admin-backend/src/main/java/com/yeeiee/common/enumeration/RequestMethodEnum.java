package com.yeeiee.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * <p>
 * 请求方法枚举
 * </p>
 *
 * @author chen
 * @since 2025-06-27
 */
@Getter
public enum RequestMethodEnum implements IEnum<Integer> {
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    PATCH(4),
    @JsonEnumDefaultValue
    UNKNOWN(-1);

    private final Integer code;

    RequestMethodEnum(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getValue() {
        return this.getCode();
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static RequestMethodEnum from(String method) {
        for (RequestMethodEnum value : values()) {
            if (value.name().equalsIgnoreCase(method)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
