package com.yeeiee.utils;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class R<T> {

    private boolean success;
    @Nullable
    private T data;
    @Nullable
    private String message;

    public static <T> R<T> ok(T data) {
        return new R<>(true, data, null);
    }

    public static <T> R<T> error(String message) {
        return new R<>(false, null, message);
    }
}
