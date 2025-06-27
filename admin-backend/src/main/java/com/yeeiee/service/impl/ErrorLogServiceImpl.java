package com.yeeiee.service.impl;

import com.yeeiee.domain.entity.ErrorLog;
import com.yeeiee.mapper.ErrorLogMapper;
import com.yeeiee.service.ErrorLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
