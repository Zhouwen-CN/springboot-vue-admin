package com.yeeiee.mapper;

import com.yeeiee.domain.entity.CodegenTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 代码生成表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-08
 */
public interface CodegenTableMapper extends BaseMapper<CodegenTable> {

    List<String> getExistsTableNameList(@Param("dataSourceId") Long dataSourceId);
}
