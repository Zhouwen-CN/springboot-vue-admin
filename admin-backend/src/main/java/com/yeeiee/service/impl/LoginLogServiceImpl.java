package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.LoginLog;
import com.yeeiee.mapper.LoginLogMapper;
import com.yeeiee.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-01-07
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
