package com.yeeiee.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * <p>
 * 定时任务状态枚举
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
@Getter
public enum JobStatusEnum implements IEnum<Integer> {
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),
    RUNNING(2, "运行中"),
    @JsonEnumDefaultValue
    UNKNOWN(-1, "未知状态");

    private final int code;
    private final String status;

    JobStatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
