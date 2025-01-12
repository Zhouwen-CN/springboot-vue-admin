package com.yeeiee.enumeration;

import lombok.Getter;

@Getter
public enum StateEnum {
    SUCCESS("成功"),
    FIELD("失败");

    private final String state;

    StateEnum(String state) {
        this.state = state;
    }
}
