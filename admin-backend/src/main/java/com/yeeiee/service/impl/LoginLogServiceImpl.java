package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StateEnum;
import com.yeeiee.mapper.LoginLogMapper;
import com.yeeiee.service.LoginLogService;
import com.yeeiee.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@AllArgsConstructor
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    private LoginLogMapper loginLogMapper;

    @Override
    public void saveLoginLog(LoginOperationEnum loginOperationEnum, StateEnum stateEnum) {
        // 写入登入日志
        val request = CommonUtil.getHttpServletRequest();
        val securityUser = CommonUtil.getSecurityUser();
        val loginLog = new LoginLog();
        loginLog.setUsername(securityUser.getUsername());
        loginLog.setOperation(loginOperationEnum.getOperation());
        loginLog.setStatus(stateEnum.getState());
        loginLog.setIp(CommonUtil.getIpAddr(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLogMapper.insert(loginLog);
    }
}
