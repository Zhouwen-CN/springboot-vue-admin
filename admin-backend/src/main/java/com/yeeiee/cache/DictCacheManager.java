package com.yeeiee.cache;

import com.yeeiee.domain.vo.DictDataVo;
import com.yeeiee.service.DictDataService;
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
    public static final String DICT_CACHE = "sv-admin:dict";
    private final DictDataService dictDataService;
    private final CacheManager cacheManager;

    /**
     * 字典缓存
     * @param typeId 字典类型id
     * @return 字典数据视图对象列表
     */
    @Cacheable(cacheNames = DICT_CACHE, key = "#typeId")
    public List<DictDataVo> getDictByTypeId(Long typeId) {
        return dictDataService.getListByTypeId(typeId);
    }

    /**
     * <pre>
     *     根据字典数据id列表批量删除字典缓存
     *     虽然字典数据id列表对应的字典一定是唯一一个，但仅仅是页面调用，工具调用没有限制
     * </pre>
     * @param ids 字典数据id列表
     */
    public void evictByDataIds(Collection<Long> ids) {
        val typeIds = dictDataService.getTypeIdByDataIds(ids);
        val cache = cacheManager.getCache(DICT_CACHE);
        if (cache != null) {
            for (Long typeId : typeIds) {
                cache.evict(typeId);
            }
        }
    }

    /**
     * 根基字典类型id列表删除字典缓存
     * @param ids 字典类型id列表
     */
    public void evictByTypeIds(Collection<Long> ids){
        val cache = cacheManager.getCache(DICT_CACHE);
        if (cache != null) {
            for (Long id : ids) {
                cache.evict(id);
            }
        }
    }
}
