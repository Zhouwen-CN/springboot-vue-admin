package com.yeeiee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.system.domain.entity.OperationLog;
import com.yeeiee.system.mapper.OperationLogMapper;
import com.yeeiee.system.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-06-27
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

}
