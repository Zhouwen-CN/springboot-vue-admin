package com.yeeiee.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import java.io.IOException;

/**
 * <p>
 * 响应工具类
 * </p>
 *
 * @author chen
 * @since 2024/05/07
 */
public final class ResponseUtil {
    public static <T> void writeResponse(HttpServletResponse response, R<T> result) throws IOException {
        val objectMapper = new ObjectMapper();
        val resultAsJson = objectMapper.writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(resultAsJson);
    }
}
