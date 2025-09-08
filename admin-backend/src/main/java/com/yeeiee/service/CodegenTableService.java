package com.yeeiee.service;

import com.yeeiee.domain.entity.CodegenTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.form.CodegenTableForm;
import com.yeeiee.domain.vo.CodegenTableVo;

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

    List<CodegenTableVo> getTableList(Long dataSourceId, String name, String comment);

    void saveCodegenTable(CodegenTableForm codegenTableForm);
}
