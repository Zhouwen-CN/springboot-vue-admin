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
import com.yeeiee.mapper.UserRoleMapper;
import com.yeeiee.security.SecurityUser;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.CollectionUtil;
import com.yeeiee.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

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
    private UserRoleMapper userRoleMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserInfoVo modifyUserAndLogin(LoginDto loginDto) {
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
        userMapper.update(new UpdateWrapper<User>()
                .lambda()
                .set(User::getTokenVersion, securityUser.getTokenVersion() + 1)
                .eq(User::getId, securityUser.getId())
        );
        return userInfoVo;
    }

    @Override
    public TokenVo modifyUserAndRefreshToken(HttpServletRequest request) {
        val token = JwtTokenUtil.getTokenFromRequest(request);
        val optional = JwtTokenUtil.parseRefreshToken(token);
        if (optional.isEmpty()) {
            throw new AuthenticationException("token验证失败");
        }

        val claimMap = optional.get();
        val username = claimMap.get("username").asString();
        val version = claimMap.get("version").asLong();

        val user = userMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username)
        );
        if (user == null || version != user.getTokenVersion() - 1) {
            throw new AuthenticationException("token验证失败");
        }

        val tokenVo = new TokenVo();
        val accessToken = JwtTokenUtil.generateAccessToken(user.getUsername(), user.getTokenVersion());
        tokenVo.setAccessToken(accessToken);
        val refreshToken = JwtTokenUtil.generateRefreshToken(user.getUsername(), user.getTokenVersion());
        tokenVo.setRefreshToken(refreshToken);

        // 更新 token version
        userMapper.update(new UpdateWrapper<User>()
                .lambda()
                .set(User::getTokenVersion, user.getTokenVersion() + 1)
                .eq(User::getId, user.getId())
        );
        return tokenVo;
    }

    @Override
    public void modifyUserAndLogout(Long id) {
        val user = userMapper.selectById(id);
        if (user != null) {
            userMapper.update(new UpdateWrapper<User>()
                    .lambda()
                    .set(User::getTokenVersion, user.getTokenVersion() + 1)
                    .eq(User::getId, user.getId())
            );
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
        val exists = userMapper.exists(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, user.getUsername())
        );
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
            userRoleMapper.insert(userRoleList);
        }
    }

    @Override
    public void modifyUserWithRoleIds(UserRoleIdsDto userRoleIdsDto) {
        val userId = userRoleIdsDto.getId();

        // 更新用户
        val password = userRoleIdsDto.getPassword();
        if (!StringUtils.hasText(password)) {
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

        // 获取当前 ids 和 更新的 ids 做差集
        val userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .lambda()
                .eq(UserRole::getUserId, userId)
        );

        val currentRoleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        val updateRoleIds = userRoleIdsDto.getRoleIds();
        val pair = CollectionUtil.differenceSet(currentRoleIds, updateRoleIds);

        // 当前 - 更新 = 删除
        val deleteSet = pair.getLeft();
        if (!deleteSet.isEmpty()) {
            val deleteUserRoles = userRoles.stream().filter(item -> deleteSet.contains(item.getRoleId())).toList();
            userRoleMapper.deleteByIds(deleteUserRoles);
        }

        // 更新 - 当前 = 新增
        val insertSet = pair.getRight();
        if (!insertSet.isEmpty()) {
            val insertUserRoles = insertSet.stream().map(id -> {
                val userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(id);
                return userRole;
            }).toList();
            userRoleMapper.insert(insertUserRoles);
        }
    }

    @Override
    public void removeUser(Long id) {
        // tip: 1号用户不能删除
        if (id == 1L) {
            throw new DmlOperationException("① 号用户不能删除");
        }
        userMapper.deleteById(id);
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .lambda()
                .eq(UserRole::getUserId, id)
        );
        userRoleMapper.deleteByIds(userRoleList);
    }

    @Override
    public void removeUsers(Collection<Long> ids) {
        if (ids.contains(1L)) {
            throw new DmlOperationException("① 号用户不能删除");
        }
        userMapper.deleteByIds(ids);
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .lambda()
                .in(UserRole::getUserId, ids)
        );
        userRoleMapper.deleteByIds(userRoleList);
    }
}
