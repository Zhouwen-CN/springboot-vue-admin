package com.yeeiee.service;

import com.yeeiee.domain.entity.CodegenTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;

import java.util.List;

/**
 * <p>
 * 代码生成表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
public interface CodegenTableService extends IService<CodegenTable> {

    /**
     * 根据数据源 id 获取表列表
     * @param dataSourceId 数据源id
     * @return 表列表
     */
    List<CodegenTableSelectorVo> getTableList(Long dataSourceId);

    /**
     * 添加代码生成表和字段
     * @param codegenTableImportForm 代码生成导入表单
     */
    void addCodegenTable(CodegenTableImportForm codegenTableImportForm);
}
