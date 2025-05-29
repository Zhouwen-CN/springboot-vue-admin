package com.yeeiee.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import java.io.IOException;

/**
 * <p>
 * 响应对象工具
 * </p>
 *
 * @author chen
 * @since 2025-05-29
 */
public final class ResponseObjectUtil {
    /**
     * 写出响应
     *
     * @param response 响应对象
     * @param result   响应体
     * @param <T>      实体类型
     * @throws IOException io 异常
     */
    public static <T> void writeResponse(HttpServletResponse response, R<T> result) throws IOException {
        val resultAsJson = JsonUtil.toJsonString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(resultAsJson);
    }
}
