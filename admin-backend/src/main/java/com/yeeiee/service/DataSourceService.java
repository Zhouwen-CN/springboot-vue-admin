package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.DataSource;
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
    /**
     * 获取数据源配置分页
     *
     * @param page       分页参数
     * @param searchName 搜索名称
     * @return 数据源配置分页
     */
    IPage<DataSourceVo> getDataSourcePage(Page<DataSourceVo> page, String searchName);
}
