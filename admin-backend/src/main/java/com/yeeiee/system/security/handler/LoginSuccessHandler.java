package com.yeeiee.system.security.handler;

import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.system.domain.entity.LoginLog;
import com.yeeiee.system.domain.entity.User;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.domain.vo.RoleSelectorVo;
import com.yeeiee.system.domain.vo.UserLoginVo;
import com.yeeiee.system.security.JwtTokenProvider;
import com.yeeiee.system.service.LoginLogService;
import com.yeeiee.system.service.RoleService;
import com.yeeiee.system.service.UserService;
import com.yeeiee.utils.IPUtil;
import com.yeeiee.utils.ResponseObjectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 登入成功处理
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RoleService roleService;
    private final LoginLogService loginLogService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        val user = (User) authentication.getCredentials();

        // 根据userId获取角色名列表
        val roles = roleService.getRoleSelectorVoListByUserId(user.getId())
                .stream()
                .map(RoleSelectorVo::getRoleName)
                .toList();

        // 封装 LoginUserVo
        val userLoginVo = new UserLoginVo();
        userLoginVo.setId(user.getId());
        userLoginVo.setUsername(user.getUsername());

        // 生成token
        val accessToken = jwtTokenProvider.generateAccessToken(user.getId(), roles);
        userLoginVo.setToken(accessToken);

        // 更新 refreshToken
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), roles);
        userService.modifyRefreshTokenByUserId(user.getId(), refreshToken);

        // 写入登入成功日志
        val loginLog = new LoginLog();
        loginLog.setOperation(LoginOperationEnum.LOGIN);
        loginLog.setStatus(OperationStatusEnum.SUCCESS);
        loginLog.setIp(IPUtil.getClientIP(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLog.setCreateUser(user.getUsername());
        loginLogService.save(loginLog);

        // 写出响应
        ResponseObjectUtil.writeJson(response, R.ok(userLoginVo));
    }
}
