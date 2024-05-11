package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.RoleDto;
import com.yeeiee.entity.dto.UserDto;
import com.yeeiee.mapper.MenuMapper;
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

import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@AllArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private AuthenticationProvider authenticationProvider;

    private UserMapper userMapper;
    private MenuMapper menuMapper;

    @Override
    public String login(LoginDto loginDto) {
        val authenticate = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return JwtTokenUtil.generateToken(authenticate);
    }

    @Override
    public UserDto getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val userDto = userMapper.selectByUserName(userDetails.getUsername());
        val roleIds = userDto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toSet());
        val menus = menuMapper.selectMenusByRoleIds(roleIds);
        userDto.setPassword(null);
        userDto.setMenus(menus);
        return userDto;
    }
}
