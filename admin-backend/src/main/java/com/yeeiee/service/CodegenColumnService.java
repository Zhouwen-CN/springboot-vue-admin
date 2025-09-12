package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.vo.CodegenColumnVo;

import java.util.List;

/**
 * <p>
 * 代码生成字段表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-09-10
 */
public interface CodegenColumnService extends IService<CodegenColumn> {

    /**
     * 根据 tableId 获取代码生成字段列表
     *
     * @param id tableId
     * @return 代码生成字段列表
     */
    List<CodegenColumnVo> getListByTableId(Long id);
}
