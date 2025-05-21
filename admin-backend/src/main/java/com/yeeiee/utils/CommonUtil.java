package com.yeeiee.utils;

import com.yeeiee.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 通用工具类
 * </p>
 *
 * @author chen
 * @since 2025-01-03
 */
public final class CommonUtil {
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
     * 从线程变量中获取 user 对象
     *
     * @return user
     */
    public static User getSecurityUser() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        val user = new User();
        user.setUsername("未知用户");
        return user;
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
