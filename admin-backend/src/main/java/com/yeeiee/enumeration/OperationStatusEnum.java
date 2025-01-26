package com.yeeiee.enumeration;

/**
 * <p>
 * 操作状态枚举类
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
public enum OperationStatusEnum {
    SUCCESS(1, "成功"),
    FIELD(0, "失败");

    private final String status;
    private final Integer code;

    OperationStatusEnum(int code, String status) {
        this.status = status;
        this.code = code;
    }

    public static String getStatus(Integer code) {
        for (OperationStatusEnum value : values()) {
            if (value.code.equals(code)) {
                return value.status;
            }
        }
        return "未知状态";
    }

    public String getStatus() {
        return this.status;
    }
}
