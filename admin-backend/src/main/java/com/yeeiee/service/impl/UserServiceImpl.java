package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.LoginLog;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.entity.UserRole;
import com.yeeiee.domain.form.ChangePasswordForm;
import com.yeeiee.domain.form.UserForm;
import com.yeeiee.domain.vo.UserRoleVo;
import com.yeeiee.domain.vo.UserVo;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.exception.VerifyTokenException;
import com.yeeiee.mapper.UserMapper;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.UserRoleService;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.CollectionUtil;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.IPUtil;
import com.yeeiee.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final LoginLogService loginLogService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    @Value("${user.default-password}")
    private String defaultPassword;

    @Override
    public UserVo refreshToken(HttpServletRequest request) {
        var refreshToken = jwtUtil.getTokenFromRequest(request);
        val optional = jwtUtil.parseRefreshToken(refreshToken);

        if (optional.isEmpty()) {
            throw new VerifyTokenException("token解析失败");
        }

        // 封装返回对象
        val claimMap = optional.get();
        val username = claimMap.get("username").asString();
        val version = claimMap.get("version").asLong();

        val user = this.lambdaQuery()
                .eq(User::getUsername, username)
                .one();

        // 检查 token version
        if (user == null || version != user.getTokenVersion() - 1) {
            throw new VerifyTokenException("token校验失败");
        }

        val userId = user.getId();
        val tokenVersion = user.getTokenVersion();

        val roleNames = claimMap.get("roleNames").asList(String.class);
        val accessToken = jwtUtil.generateAccessToken(username, roleNames, tokenVersion);
        refreshToken = jwtUtil.generateRefreshToken(username, roleNames, tokenVersion);

        val userVo = new UserVo();
        userVo.setId(userId);
        userVo.setUsername(username);
        userVo.setAccessToken(accessToken);
        userVo.setRefreshToken(refreshToken);

        // 更新 token version
        this.lambdaUpdate()
                .set(User::getTokenVersion, tokenVersion + 1)
                .eq(User::getId, userId)
                .update();

        return userVo;
    }

    @Override
    public void logout(Long id) {
        val user = this.getById(id);

        // 更新token version
        if (user != null) {
            this.lambdaUpdate()
                    .set(User::getTokenVersion, user.getTokenVersion() + 1)
                    .eq(User::getId, user.getId())
                    .update();

            // 退出登入日志
            val request = CommonUtil.getHttpServletRequest();
            val loginLog = new LoginLog();
            loginLog.setUsername(user.getUsername());
            loginLog.setOperation(LoginOperationEnum.LOGOUT.getOperation());
            loginLog.setStatus(OperationStatusEnum.SUCCESS.getStatus());
            loginLog.setIp(IPUtil.getClientIP(request));
            loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            loginLogService.save(loginLog);
        }
    }

    @Override
    public IPage<UserRoleVo> getUserPages(Page<UserRoleVo> page, String searchName) {
        return userMapper.selectUserPages(page, searchName);
    }

    @Override
    public void addUser(UserForm userForm) {
        val username = userForm.getUsername();
        val password = userForm.getPassword();

        val exists = this.exists(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );

        if (exists) {
            throw new DmlOperationException("用户名已经存在");
        }

        val user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        this.save(user);

        if (!userForm.getRoleIds().isEmpty()) {
            val userRoleList = userForm.getRoleIds().stream().map(roleId -> {
                val userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                return userRole;
            }).toList();
            userRoleService.saveBatch(userRoleList);
        }
    }

    @Override
    public void modifyUser(UserForm userForm) {
        val userId = userForm.getId();
        val username = userForm.getUsername();

        // todo: admin自己才能修改自己
        val securityUser = CommonUtil.getSecurityUser();
        if (userId == 1 && securityUser.getId() != 1) {
            throw new DmlOperationException("不能修改 ① 号用户");
        }

        // 更新用户
        this.lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getUsername, username)
                .setIncrBy(User::getTokenVersion, 1)
                .update();

        // 获取 user role 关系
        val userRoles = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .list();

        // 获取当前 ids 和 更新的 ids 做差集
        val currentRoleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        val updateRoleIds = userForm.getRoleIds();
        val pair = CollectionUtil.differenceSet(currentRoleIds, updateRoleIds);

        // 当前 - 更新 = 删除
        val deleteSet = pair.getLeft();
        if (!deleteSet.isEmpty()) {
            val deleteUserRoles = userRoles.stream().filter(item -> deleteSet.contains(item.getRoleId())).toList();
            userRoleService.removeByIds(deleteUserRoles);
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
            userRoleService.saveBatch(insertUserRoles);
        }
    }

    @Override
    public void removeUserById(Long id) {
        // todo: 1 号用户不能删除
        if (id == 1L) {
            throw new DmlOperationException("① 号用户不能删除");
        }

        this.removeById(id);
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, id)
        );
    }

    @Override
    public void removeUserByIds(Collection<Long> ids) {
        if (ids.contains(1L)) {
            throw new DmlOperationException("① 号用户不能删除");
        }

        this.removeByIds(ids);

        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getUserId, ids)
        );
    }

    @Override
    public void modifyUserChangePwd(ChangePasswordForm changePasswordForm) {
        val id = changePasswordForm.getId();
        val oldPwd = changePasswordForm.getOldPwd();

        val user = this.lambdaQuery()
                .eq(User::getId, id)
                .one();

        if (!bCryptPasswordEncoder.matches(oldPwd, user.getPassword())) {
            throw new DmlOperationException("旧密码错误");
        }

        this.lambdaUpdate()
                .eq(User::getId, id)
                .set(User::getPassword, bCryptPasswordEncoder.encode(changePasswordForm.getNewPwd()))
                .setIncrBy(User::getTokenVersion, 1)
                .update();
    }

    @Override
    public void modifyUserResetPwd(Long id) {
        this.lambdaUpdate()
                .eq(User::getId, id)
                .set(User::getPassword, bCryptPasswordEncoder.encode(defaultPassword))
                .setIncrBy(User::getTokenVersion, 1)
                .update();
    }
}
