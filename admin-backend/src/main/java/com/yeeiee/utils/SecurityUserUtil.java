package com.yeeiee.utils;

import com.yeeiee.system.domain.entity.User;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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
        if (authentication != null && authentication.getCredentials() instanceof User) {
            return (User) authentication.getCredentials();
        }

        val user = new User();
        user.setUsername("未知用户");
        return user;
    }

    /**
     * 获取用户名
     * @return 用户名
     */
    public static Optional<String> getPrincipal(){
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(auth -> String.valueOf(auth.getPrincipal()));
    }
}
