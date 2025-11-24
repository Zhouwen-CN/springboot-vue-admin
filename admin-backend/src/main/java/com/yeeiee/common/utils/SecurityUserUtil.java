package com.yeeiee.common.utils;

import com.yeeiee.system.domain.entity.User;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

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

    public static List<String> getAuthorities() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
