package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Map;

/**
 * <p>
 * 分页条件，默认为空，需要定制化分页则重写方法
 * </p>
 *
 * @param <D> entity类型
 * @author chen
 * @since 2025-06-06
 */
@Deprecated
public interface BaseControllerPageHelper<D> {
    /**
     * 分页条件
     *
     * @param params url 请求参数
     * @return query wrapper
     */
    default Wrapper<D> pageHelper(Map<String, String> params) {
        return Wrappers.emptyWrapper();
    }
}
