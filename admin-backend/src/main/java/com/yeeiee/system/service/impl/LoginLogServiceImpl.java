package com.yeeiee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.system.domain.entity.LoginLog;
import com.yeeiee.system.mapper.LoginLogMapper;
import com.yeeiee.system.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-06-27
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
