package com.yeeiee.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 请求对象工具
 * </p>
 *
 * @author chen
 * @since 2025-05-29
 */
public final class RequestObjectUtil {
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 从请求头中获取token
     *
     * @param request 请求对象
     * @return token
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        val bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 从线程变量中获取 request 对象
     *
     * @return request
     */
    public static HttpServletRequest getHttpServletRequest() {
        val requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 从 request 中获取请求参数
     *
     * @param request 请求对象
     * @return 请求参数
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        val parameters = request.getParameterNames();

        val params = new HashMap<String, String>();
        while (parameters.hasMoreElements()) {
            val parameter = parameters.nextElement();
            val value = request.getParameter(parameter);
            if (StringUtils.hasText(value)) {
                params.put(parameter, value);
            }
        }
        return params;
    }
}
