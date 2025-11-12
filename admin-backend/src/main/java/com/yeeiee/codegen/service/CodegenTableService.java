package com.yeeiee.codegen.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.codegen.domain.entity.CodegenTable;
import com.yeeiee.codegen.domain.form.CodegenTableColumnsForm;
import com.yeeiee.codegen.domain.form.CodegenTableImportForm;
import com.yeeiee.codegen.domain.vo.CodegenTableSelectorVo;
import com.yeeiee.codegen.domain.vo.CodegenTableVo;

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
     * @param wrapper 关键字查询条件
     * @return 代码生成表分页
     */
    IPage<CodegenTableVo> getCodegenTablePage(Page<CodegenTableVo> page, Wrapper<CodegenTable> wrapper);

    /**
     * 修改代码生成配置
      * @param codegenTableColumnsForm 代码生成配置表单
     */
    void modifyCodegenConfig(CodegenTableColumnsForm codegenTableColumnsForm);
}
