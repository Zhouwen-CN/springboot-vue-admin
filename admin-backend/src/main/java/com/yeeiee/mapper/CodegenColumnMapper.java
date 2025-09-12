package com.yeeiee.mapper;

import com.yeeiee.domain.entity.CodegenColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.vo.CodegenColumnVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 代码生成字段表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-12
 */
public interface CodegenColumnMapper extends BaseMapper<CodegenColumn> {

    List<CodegenColumnVo> selectListByTableId(@Param("tableId") Long id);
}
