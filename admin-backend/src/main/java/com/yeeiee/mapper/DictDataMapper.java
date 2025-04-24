package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.vo.DictDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典数据表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
public interface DictDataMapper extends BaseMapper<DictData> {
    List<DictDataVo> selectListByType(@Param("type") String type);
}
