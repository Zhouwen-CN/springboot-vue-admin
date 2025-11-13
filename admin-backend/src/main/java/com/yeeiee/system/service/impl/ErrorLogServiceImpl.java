package com.yeeiee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.system.domain.entity.ErrorLog;
import com.yeeiee.system.mapper.ErrorLogMapper;
import com.yeeiee.system.service.ErrorLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 错误日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-06-27
 */
@Service
public class ErrorLogServiceImpl extends ServiceImpl<ErrorLogMapper, ErrorLog> implements ErrorLogService {

}
