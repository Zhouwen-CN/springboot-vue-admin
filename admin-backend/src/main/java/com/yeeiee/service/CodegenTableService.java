package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.form.CodegenTableColumnsForm;
import com.yeeiee.domain.form.CodegenTableImportForm;
import com.yeeiee.domain.vo.CodegenTableSelectorVo;
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

    /**
     * 根据数据源 id 获取代码生成表选择器
     *
     * @param dataSourceId 数据源id
     * @return 表列表
     */
    List<CodegenTableSelectorVo> getCodegenTableSelector(Long dataSourceId);

    /**
     * 添加代码生成表和字段
     *
     * @param codegenTableImportForm 代码生成导入表单
     */
    void addCodegenTable(CodegenTableImportForm codegenTableImportForm);

    /**
     * reset 代码生成列
     *
     * @param id 代码生成表id
     */
    void modifyCodegenColumnList(Long id);

    /**
     * 获取代码生成表分页
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 代码生成表分页
     */
    IPage<CodegenTableVo> getCodegenTablePage(Page<CodegenTableVo> page, String keyword);

    /**
     * 修改代码生成配置
      * @param codegenTableColumnsForm 代码生成配置表单
     */
    void modifyCodegenConfig(CodegenTableColumnsForm codegenTableColumnsForm);
}
