package com.yeeiee.security.handler;

import com.yeeiee.entity.LoginLog;
import com.yeeiee.entity.User;
import com.yeeiee.entity.vo.UserInfoVo;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StateEnum;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.service.RoleService;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.JwtTokenUtil;
import com.yeeiee.utils.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 登入失败处理
 */

@AllArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    private RoleService roleService;

    private LoginLogService loginLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1.封装 LoginUserVo
        val user = (User) authentication.getPrincipal();
        val roles = roleService.getRoleListByUserId(user.getId());

        val loginUserVo = new UserInfoVo();
        loginUserVo.setId(user.getId());
        loginUserVo.setUsername(user.getUsername());
        loginUserVo.setRoleList(roles);

        val accessToken = JwtTokenUtil.generateAccessToken(user.getUsername(), user.getTokenVersion());
        loginUserVo.setAccessToken(accessToken);
        val refreshToken = JwtTokenUtil.generateRefreshToken(user.getUsername(), user.getTokenVersion());
        loginUserVo.setRefreshToken(refreshToken);

        // 2.更新 token version
        userService.lambdaUpdate()
                .set(User::getTokenVersion, user.getTokenVersion() + 1)
                .eq(User::getId, user.getId())
                .update();

        // 3.写入登入成功日志
        val loginLog = new LoginLog();
        loginLog.setUsername(user.getUsername());
        loginLog.setOperation(LoginOperationEnum.LOGIN.getOperation());
        loginLog.setStatus(StateEnum.SUCCESS.getState());
        loginLog.setIp(CommonUtil.getIpAddr(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLogService.save(loginLog);

        // 4.写出响应
        CommonUtil.writeResponse(response, R.ok(loginUserVo));
    }
}
