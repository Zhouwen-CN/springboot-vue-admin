package com.yeeiee.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class JwtUserDetailServiceImpl implements UserDetailsService {

    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = userMapper.selectOne(new QueryWrapper<com.yeeiee.entity.User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("cannot find username: " + username);
        }
        // TODO: 这里暂时不做授权
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
