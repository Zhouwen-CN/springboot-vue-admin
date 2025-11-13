package com.yeeiee.cache;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yeeiee.system.domain.entity.DictData;
import com.yeeiee.system.domain.entity.DictType;
import com.yeeiee.system.domain.vo.DictDataSelectorVo;
import com.yeeiee.system.service.DictDataService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典缓存管理
 * </p>
 *
 * @author chen
 * @since 2025-06-17
 */


@RequiredArgsConstructor
@Component
public class DictCacheManager {
    private final DictDataService dictDataService;
    private final CacheManager cacheManager;
    public static final String DICT_CACHE = "dict";

    /**
     * 字典缓存
     *
     * @param typeId 字典类型id
     * @return 字典数据视图对象列表
     */
    @Cacheable(cacheNames = DICT_CACHE, key = "#p0")
    public List<DictDataSelectorVo> getDictByTypeId(Long typeId) {
        // xml sql涉及boolean，尽量使用queryWrapper
        val queryWrapper = Wrappers.<DictType>lambdaQuery()
                .eq(DictType::getId, typeId)
                .eq(DictType::getDictEnable, true);
        return dictDataService.getDictByTypeId(queryWrapper);
    }

    /**
     * <pre>
     *     根据字典数据id列表批量删除字典缓存
     *     虽然字典数据id列表对应的字典一定是唯一一个，但仅仅是页面调用，工具调用没有限制
     * </pre>
     *
     * @param ids 字典数据id列表
     */
    public void evictByDataIds(Collection<Long> ids) {
        val typeIds = dictDataService.lambdaQuery()
                .select(DictData::getTypeId)
                .in(DictData::getId, ids)
                .groupBy(DictData::getTypeId)
                .list()
                .stream()
                .map(DictData::getTypeId)
                .toList();

        val cache = cacheManager.getCache(DICT_CACHE);
        if (cache != null) {
            for (Long typeId : typeIds) {
                cache.evict(typeId);
            }
        }
    }

    /**
     * 根基字典类型id列表删除字典缓存
     *
     * @param ids 字典类型id列表
     */
    public void evictByTypeIds(Collection<Long> ids) {
        val cache = cacheManager.getCache(DICT_CACHE);
        if (cache != null) {
            for (Long id : ids) {
                cache.evict(id);
            }
        }
    }
}
