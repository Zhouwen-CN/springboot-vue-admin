package com.yeeiee.cache;

import com.yeeiee.domain.entity.User;
import com.yeeiee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * <p>
 * 用户缓存管理器
 * </p>
 *
 * @author chen
 * @since 2025-06-19
 */
@Component
@RequiredArgsConstructor
public class UserCacheManager {
    private final UserService userService;
    private final CacheManager cacheManager;
    public static final String USER_CACHE = "sv-admin::user";

    @Cacheable(cacheNames = USER_CACHE, key = "#username",condition = "#isCacheNeeded = true")
    public User getUserByUsername(String username, boolean isCacheNeeded) {
        return userService.lambdaQuery()
                .eq(User::getUsername, username)
                .one();
    }

    @CacheEvict(cacheNames = USER_CACHE, key = "#user.username")
    public void updateUserTokenVersion(User user) {
        userService.lambdaUpdate()
                .eq(User::getId, user.getId())
                .set(User::getTokenVersion, user.getTokenVersion() + 1)
                .update();
    }

    /**
     * 根据用户id列表清除缓存
     * @param ids 用户id列表
     */
    public void evictByUserIds(Collection<Long> ids) {
        val list = userService.lambdaQuery()
                .in(User::getId, ids)
                .list();

        for (User user : list) {
            val username = user.getUsername();
            val cache = cacheManager.getCache(USER_CACHE);
            if (cache != null) {
                cache.evict(username);
            }
        }
    }
}
