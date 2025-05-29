package com.yeeiee.utils;

import com.yeeiee.domain.entity.User;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * security user工具
 * </p>
 *
 * @author chen
 * @since 2025-05-29
 */
public class SecurityUserUtil {
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
}
