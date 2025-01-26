package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.entity.User;
import com.yeeiee.entity.UserRole;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.UserRoleVo;
import com.yeeiee.entity.vo.UserVo;
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
import com.yeeiee.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
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
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public static final String BCRYPT_PREFIX = "$2a$10$";
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final LoginLogService loginLogService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserVo refreshToken(HttpServletRequest request) {
        var refreshToken = JwtTokenUtil.getTokenFromRequest(request);
        val optional = JwtTokenUtil.parseRefreshToken(refreshToken);

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


        val roleNames = claimMap.get("roleNames").asList(String.class);
        val accessToken = JwtTokenUtil.generateAccessToken(username, roleNames, user.getTokenVersion());
        refreshToken = JwtTokenUtil.generateRefreshToken(username, roleNames, user.getTokenVersion());

        val userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUsername(username);
        userVo.setAccessToken(accessToken);
        userVo.setRefreshToken(refreshToken);

        // 更新 token version
        this.lambdaUpdate()
                .set(User::getTokenVersion, user.getTokenVersion() + 1)
                .eq(User::getUsername, username)
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
            loginLog.setIp(CommonUtil.getIpAddr(request));
            loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            loginLogService.save(loginLog);
        }


    }

    @Override
    public IPage<UserRoleVo> getUserPages(Page<UserRoleVo> page, String searchName) {
        return userMapper.selectUserPages(page, searchName);
    }

    @Override
    public void addUser(UserRoleIdsDto userRoleIdsDto) {
        val username = userRoleIdsDto.getUsername();
        val password = userRoleIdsDto.getPassword();

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

    /*
        todo: 直接判断 password.startsWith 是不是不太合适
     */
    @Override
    public void modifyUser(UserRoleIdsDto userRoleIdsDto) {
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

        this.updateById(user);

        // 获取 user role 关系
        val userRoles = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .list();

        // 获取当前 ids 和 更新的 ids 做差集
        val currentRoleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        val updateRoleIds = userRoleIdsDto.getRoleIds();
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
        // tip: 1号用户不能删除
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
}
