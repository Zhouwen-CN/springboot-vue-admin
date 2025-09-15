package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.mapper.CodegenColumnMapper;
import com.yeeiee.service.CodegenColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 代码生成字段表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-09-10
 */
@RequiredArgsConstructor
@Service
public class CodegenColumnServiceImpl extends ServiceImpl<CodegenColumnMapper, CodegenColumn> implements CodegenColumnService {
}
