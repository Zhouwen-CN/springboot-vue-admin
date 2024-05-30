package com.yeeiee.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * 统一响应对象
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(title = "R", description = "统一响应对象")
public final class R<T> {

    @Schema(title = "是否成功")
    private boolean success;

    @Nullable
    @Schema(title = "数据")
    private T data;

    @Schema(title = "状态码")
    private int code;

    @Nullable
    @Schema(title = "消息")
    private String message;

    /**
     * 返回ok字符串，前端可以利用字符串进行判断
     * 比如封装useRequest的时候，data已经被取出，还要判断请求是否成功
     */
    public static R<String> ok() {
        return new R<>(true, "ok", 200, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, data, 200, null);
    }

    public static R<Void> error(HttpStatus httpStatus) {
        return new R<>(false, null, httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static R<Void> error(HttpStatus httpStatus, Exception e) {
        return new R<>(false, null, httpStatus.value(), e.getMessage());
    }

    public static R<Void> error(HttpStatus httpStatus, String message) {
        return new R<>(false, null, httpStatus.value(), message);
    }
}
