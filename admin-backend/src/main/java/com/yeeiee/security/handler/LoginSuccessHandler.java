package com.yeeiee.security.handler;

import com.yeeiee.domain.entity.LoginLog;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.vo.R;
import com.yeeiee.domain.vo.UserVo;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.OperationStatusEnum;
import com.yeeiee.security.JwtTokenProvider;
import com.yeeiee.security.user.UserAuthenticationToken;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.RoleService;
import com.yeeiee.service.UserService;
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
 * 登入失败处理
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
        val user = (User) authentication.getPrincipal();

        // 根据userId获取角色名列表
        val roles = roleService.getRoleNamesByUserId(user.getId());

        // 封装 LoginUserVo
        val loginUserVo = new UserVo();
        loginUserVo.setId(user.getId());
        loginUserVo.setUsername(user.getUsername());
        loginUserVo.setRoles(roles);

        // 生成token
        val accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), roles, user.getTokenVersion());
        loginUserVo.setAccessToken(accessToken);
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), roles, user.getTokenVersion());
        loginUserVo.setRefreshToken(refreshToken);

        // 更新 token version
        userService.modifyTokenVersionByUser(user);

        // 写入登入成功日志（刷新token不需要写）
        if (authentication instanceof UserAuthenticationToken) {
            val loginLog = new LoginLog();
            loginLog.setUsername(user.getUsername());
            loginLog.setOperation(LoginOperationEnum.LOGIN);
            loginLog.setStatus(OperationStatusEnum.SUCCESS);
            loginLog.setIp(IPUtil.getClientIP(request));
            loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            loginLogService.save(loginLog);
        }

        // 写出响应
        ResponseObjectUtil.writeResponse(response, R.ok(loginUserVo));
    }
}
