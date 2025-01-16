package com.yeeiee.security.handler;

import com.yeeiee.entity.LoginLog;
import com.yeeiee.entity.User;
import com.yeeiee.entity.vo.RoleVo;
import com.yeeiee.entity.vo.UserVo;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StatusEnum;
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
        // 封装 LoginUserVo
        val user = (User) authentication.getPrincipal();

        val roleVoList = roleService.getRoleListByUserId(user.getId());

        val loginUserVo = new UserVo();
        loginUserVo.setId(user.getId());
        loginUserVo.setUsername(user.getUsername());
        loginUserVo.setRoleList(roleVoList);

        val roleNames = roleVoList.stream()
                .map(RoleVo::getRoleName)
                .toList();

        // 生成token
        val accessToken = JwtTokenUtil.generateAccessToken(user.getUsername(), roleNames, user.getTokenVersion());
        loginUserVo.setAccessToken(accessToken);
        val refreshToken = JwtTokenUtil.generateRefreshToken(user.getUsername(), roleNames, user.getTokenVersion());
        loginUserVo.setRefreshToken(refreshToken);

        // 更新 token version
        userService.lambdaUpdate()
                .set(User::getTokenVersion, user.getTokenVersion() + 1)
                .eq(User::getId, user.getId())
                .update();

        // 写入登入成功日志
        val loginLog = new LoginLog();
        loginLog.setUsername(user.getUsername());
        loginLog.setOperation(LoginOperationEnum.LOGIN.getOperation());
        loginLog.setStatus(StatusEnum.SUCCESS.getStatus());
        loginLog.setIp(CommonUtil.getIpAddr(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLogService.save(loginLog);

        // 写出响应
        CommonUtil.writeResponse(response, R.ok(loginUserVo));
    }
}
