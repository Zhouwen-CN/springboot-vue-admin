package com.yeeiee.service.impl;

import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.mapper.UserMapper;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private AuthenticationProvider authenticationProvider;

    private UserMapper userMapper;

    @Override
    public String login(LoginDto loginDto) {
        val authenticate = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return JwtTokenUtil.generateToken(authenticate);
    }

    @Override
    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.getUserByName(userDetails.getUsername());
    }
}
