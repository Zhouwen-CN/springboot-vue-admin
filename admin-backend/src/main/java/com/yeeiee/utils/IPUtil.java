package com.yeeiee.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 从 request 中获取真实的请求 ip
 * </p>
 *
 * @author chen
 * @since 2025-05-21
 */
public final class IPUtil {

    public static String getClientIP(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"};

        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValidIP(ip)) {
                return getFirstValidIP(ip);
            }
        }

        ip = request.getRemoteAddr();
        return getFirstValidIP(ip);
    }

    private static boolean isValidIP(String ip) {
        return StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    private static String getFirstValidIP(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            val ips = ip.split(",");
            for (String subIp : ips) {
                subIp = subIp.trim();
                if (isValidIP(subIp)) {
                    return subIp;
                }
            }
        }
        return ip;
    }
}
