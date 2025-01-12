package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.enumeration.LoginOperationEnum;
import com.yeeiee.enumeration.StateEnum;

/**
 * <p>
 * 登录日志表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginOperationEnum loginOperationEnum, StateEnum stateEnum);
}
