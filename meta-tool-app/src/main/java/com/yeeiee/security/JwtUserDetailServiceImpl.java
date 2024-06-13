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
public class JwtUserDetailServiceImpl implements UserDetailsService {

    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val userRoleVo = userMapper.selectByUserName(username);
        if (userRoleVo == null) {
            throw new UsernameNotFoundException("cannot find username: " + username);
        }

        val userBuilder = User.builder()
                .username(userRoleVo.getUsername())
                .password(userRoleVo.getPassword());

        // 授权角色
        val roleIds = userRoleVo.getRoleIds();
        if (roleIds != null) {
            userBuilder.roles(roleIds.split(","));
        }
        return userBuilder.build();
    }
}
