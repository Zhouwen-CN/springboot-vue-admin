package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.vo.CodegenTableVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 代码生成表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-10
 */
public interface CodegenTableMapper extends BaseMapper<CodegenTable> {

    List<String> getExistsTableNameList(@Param("dataSourceId") Long dataSourceId);

    IPage<CodegenTableVo> selectCodegenTablePage(Page<CodegenTableVo> page, @Param("searchName") String searchName);
}
