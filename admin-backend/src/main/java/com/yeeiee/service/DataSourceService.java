package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.DataSource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.vo.DataSourceVo;

/**
 * <p>
 * 数据源配置表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
public interface DataSourceService extends IService<DataSource> {

    IPage<DataSourceVo> getDataSourcePage(Page<DataSourceVo> page, String searchName);
}
