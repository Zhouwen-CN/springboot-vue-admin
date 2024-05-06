package com.yeeiee.security;

import com.yeeiee.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * <p>
 * user details service: 从数据库查询用户
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Component
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val securityUser = userMapper.getUserByName(username);
        if (securityUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .username(securityUser.getUsername())
                .password(securityUser.getPassword())
                .authorities(securityUser.getAuthorities().toArray(new String[0]))
                .build();
    }
}
