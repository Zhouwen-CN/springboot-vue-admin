package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.vo.DataSourceVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数据源配置表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
public interface DataSourceMapper extends BaseMapper<DataSource> {

    IPage<DataSourceVo> selectDataSourcePage(Page<DataSourceVo> page,@Param("searchName") String searchName);
}
