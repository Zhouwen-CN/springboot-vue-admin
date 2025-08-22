package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DataSource;
import com.yeeiee.domain.vo.DataSourceVo;
import com.yeeiee.mapper.DataSourceMapper;
import com.yeeiee.service.DataSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据源配置表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {
    private final DataSourceMapper dataSourceMapper;

    @Override
    public IPage<DataSourceVo> getDataSourcePage(Page<DataSourceVo> page, String searchName) {
        return dataSourceMapper.selectDataSourcePage(page, searchName);
    }
}
