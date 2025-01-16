package com.yeeiee.enumeration;

public enum StatusEnum {
    SUCCESS(1, "成功"),
    FIELD(0, "失败");

    private final String status;
    private final Integer code;

    StatusEnum(int code, String status) {
        this.status = status;
        this.code = code;
    }

    public static String getStatus(Integer code) {
        for (StatusEnum value : values()) {
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
