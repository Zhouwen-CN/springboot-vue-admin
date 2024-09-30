package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.User;
import com.yeeiee.entity.UserRole;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.TokenVo;
import com.yeeiee.entity.vo.UserInfoVo;
import com.yeeiee.entity.vo.UserRoleVo;
import com.yeeiee.exception.AuthenticationException;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.UserMapper;
import com.yeeiee.security.SecurityUser;
import com.yeeiee.service.UserRoleService;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
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
    public static final String BCRYPT_PREFIX = "$2a$10$";
    private AuthenticationProvider authenticationProvider;
    private UserMapper userMapper;
    private UserRoleService userRoleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserInfoVo modifyUserWithLogin(LoginDto loginDto) {
        val authenticate = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        val securityUser = (SecurityUser) authenticate.getPrincipal();
        val userInfoVo = new UserInfoVo();
        userInfoVo.setId(securityUser.getId());
        userInfoVo.setUsername(securityUser.getUsername());

        val accessToken = JwtTokenUtil.generateAccessToken(securityUser.getUsername(), securityUser.getTokenVersion());
        userInfoVo.setAccessToken(accessToken);
        val refreshToken = JwtTokenUtil.generateRefreshToken(securityUser.getUsername(), securityUser.getTokenVersion());
        userInfoVo.setRefreshToken(refreshToken);
        userInfoVo.setRoleList(securityUser.getRoleList());

        // 更新 token version
        val updateWrapper = new UpdateWrapper<User>();
        updateWrapper.eq("id", securityUser.getId())
                .set("token_version", securityUser.getTokenVersion() + 1);
        userMapper.update(updateWrapper);

        return userInfoVo;
    }

    @Override
    public TokenVo modifyUserWithRefreshToken(HttpServletRequest request) {
        val token = JwtTokenUtil.getTokenFromRequest(request);
        val optional = JwtTokenUtil.parseRefreshToken(token);
        if (optional.isEmpty()) {
            throw new AuthenticationException("token验证失败");
        }

        val claimMap = optional.get();
        val username = claimMap.get("username").asString();
        val version = claimMap.get("version").asLong();

        val queryWrapper = new QueryWrapper<User>().eq("username", username);
        val user = userMapper.selectOne(queryWrapper);
        if (user == null || version != user.getTokenVersion() - 1) {
            throw new AuthenticationException("token验证失败");
        }

        val tokenVo = new TokenVo();
        val accessToken = JwtTokenUtil.generateAccessToken(user.getUsername(), user.getTokenVersion());
        tokenVo.setAccessToken(accessToken);
        val refreshToken = JwtTokenUtil.generateRefreshToken(user.getUsername(), user.getTokenVersion());
        tokenVo.setRefreshToken(refreshToken);

        // 更新 token version
        val updateWrapper = new UpdateWrapper<User>();
        updateWrapper.eq("id", user.getId())
                .set("token_version", user.getTokenVersion() + 1);
        userMapper.update(updateWrapper);
        return tokenVo;
    }

    @Override
    public void modifyUserWithLogout(Long id) {
        val user = userMapper.selectById(id);
        if (user != null) {
            val updateWrapper = new UpdateWrapper<User>();
            updateWrapper.eq("id", user.getId())
                    .set("token_version", user.getTokenVersion() + 1);
            userMapper.update(updateWrapper);
        }
    }

    @Override
    public IPage<UserRoleVo> getUserPages(Page<UserRoleVo> page, String searchName) {
        return userMapper.selectUserPages(page, searchName);
    }

    @Override
    public void addUserWithRoleIds(UserRoleIdsDto userRoleIdsDto) {
        val user = new User();
        user.setUsername(userRoleIdsDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRoleIdsDto.getPassword()));
        val exists = userMapper.exists(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (exists) {
            throw new DmlOperationException("用户名已经存在");
        }
        userMapper.insert(user);

        if (!userRoleIdsDto.getRoleIds().isEmpty()) {
            val userRoleList = userRoleIdsDto.getRoleIds().stream().map(roleId -> {
                val userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                return userRole;
            }).toList();
            userRoleService.saveBatch(userRoleList);
        }
    }

    @Override
    public void modifyUserWithRoleIds(UserRoleIdsDto userRoleIdsDto) {
        val userId = userRoleIdsDto.getId();

        // 更新用户
        val password = userRoleIdsDto.getPassword();
        if (password == null || password.trim().isEmpty()) {
            throw new DmlOperationException("密码不能为空");
        }
        // 所有加密的密码都是以这个开头的
        if (!password.startsWith(BCRYPT_PREFIX)) {
            userRoleIdsDto.setPassword(bCryptPasswordEncoder.encode(password));
        }
        val user = new User();
        user.setId(userRoleIdsDto.getId());
        user.setUsername(userRoleIdsDto.getUsername());
        user.setPassword(userRoleIdsDto.getPassword());
        userMapper.updateById(user);

        // 获取当前ids 和 更新的ids 做差集
        val queryWrapper = new QueryWrapper<UserRole>();
        queryWrapper.eq("user_id", userId);
        val userRoleList = userRoleService.list(queryWrapper);
        val userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(UserRole::getRoleId));
        val currentSet = userRoleMap.keySet();
        val updateSet = new HashSet<>(userRoleIdsDto.getRoleIds());

        // 当前 - 更新 = 删除
        val deleteSet = new HashSet<>(currentSet);
        deleteSet.removeAll(updateSet);
        if (!deleteSet.isEmpty()) {
            val deleteUserRoles = deleteSet.stream().flatMap(id -> userRoleMap.get(id).stream()).toList();
            userRoleService.removeBatchByIds(deleteUserRoles);
        }

        // 更新 - 当前 = 新增
        val additionSet = new HashSet<>(updateSet);
        additionSet.removeAll(currentSet);
        if (!additionSet.isEmpty()) {
            val additionUserRoleList = additionSet.stream().map(id -> {
                val userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(id);
                return userRole;
            }).toList();

            userRoleService.saveBatch(additionUserRoleList);
        }
    }

    @Override
    public void removeUser(Long id) {
        // tip: 1号用户不能删除
        if (id == 1L) {
            throw new DmlOperationException("① 号用户不能删除");
        }
        userMapper.deleteById(id);
        val queryWrapper = new QueryWrapper<UserRole>();
        queryWrapper.eq("user_id", id);
        val userRoleList = userRoleService.list(queryWrapper);
        userRoleService.removeBatchByIds(userRoleList);
    }

    @Override
    public void removeUsers(Collection<Long> ids) {
        if (ids.contains(1L)) {
            throw new DmlOperationException("① 号用户不能删除");
        }
        userMapper.deleteBatchIds(ids);
        val queryWrapper = new QueryWrapper<UserRole>();
        queryWrapper.in("user_id", ids);
        val userRoleList = userRoleService.list(queryWrapper);
        userRoleService.removeBatchByIds(userRoleList);
    }
}
