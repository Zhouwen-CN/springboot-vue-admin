package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
@Schema(title = "R", description = "响应视图对象")
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

    public static R<Void> ok() {
        return new R<>(true, null, 200, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, data, 200, null);
    }

    public static R<Void> error(HttpStatus httpStatus, Exception e) {
        return new R<>(false, null, httpStatus.value(), ExceptionUtils.getRootCauseMessage(e));
    }

    public static R<Void> error(HttpStatus httpStatus, String message) {
        return new R<>(false, null, httpStatus.value(), message);
    }
}
