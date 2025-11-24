package com.yeeiee.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * <p>
 * 操作状态枚举类
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Getter
public enum OperationStatusEnum implements IEnum<Integer> {
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),
    @JsonEnumDefaultValue
    UNKNOWN(-1, "未知状态");

    private final String status;
    private final Integer code;

    OperationStatusEnum(int code, String status) {
        this.status = status;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.status;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
