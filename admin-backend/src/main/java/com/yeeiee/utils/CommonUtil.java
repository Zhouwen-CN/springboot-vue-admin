package com.yeeiee.utils;

import com.yeeiee.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

public final class CommonUtil {
    /**
     * 从线程变量中获取 request 对象
     *
     * @return request
     */
    public static HttpServletRequest getHttpServletRequest() {

        val requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 从线程变量中获取 user 对象
     *
     * @return user
     */
    public static SecurityUser getSecurityUser() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
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
     * 从 request 中获取真实的请求 ip
     *
     * @param request 请求对象
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        val unknown = "unknown";

        String[] headerKeys = {
                // 只有在通过了 HTTP 代理或者负载均衡服务器时才会添加该项。格式为X-Forwarded-For: client1, proxy1, proxy2
                "x-forwarded-for",
                // 经过apache http服务器的请求才会有，用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL-Proxy-Client-IP是他的 weblogic 插件加上的头
                "proxy-client-ip",
                "wl-proxy-client-ip",
                // 有些代理服务器会加上此请求头
                "http_client_ip",
                "http_x_forwarded_for",
                // nginx代理一般会加上此请求头
                "x-real-ip"
        };

        for (String key : headerKeys) {
            val header = request.getHeader(key);
            if (StringUtils.hasLength(header) && !unknown.equalsIgnoreCase(header)) {
                return header;
            }
        }

        return request.getRemoteAddr();
    }
}
