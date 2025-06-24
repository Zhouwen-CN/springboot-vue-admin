package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.cache.DictCacheManager;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.entity.DictType;
import com.yeeiee.domain.form.DictTypeForm;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.DictTypeMapper;
import com.yeeiee.service.DictDataService;
import com.yeeiee.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    private final DictDataService dictDataService;
    private final DictCacheManager dictCacheManager;
    @Override
    public void addDictType(DictTypeForm dictTypeForm) {
        val exists = this.lambdaQuery()
                .eq(DictType::getType, dictTypeForm.getType())
                .exists();

        if (exists) {
            throw new DmlOperationException("字典类型已存在");
        }
        this.save(dictTypeForm.toBean());
    }

    @Override
    public void removeDictTypeByIds(Collection<Long> ids) {
        val dictDataList = dictDataService.lambdaQuery()
                .in(DictData::getTypeId, ids)
                .list();

        if (!dictDataList.isEmpty()) {
            throw new DmlOperationException("删除失败，尚有字典数据依赖");
        }

        dictCacheManager.evictByTypeIds(ids);
        this.removeByIds(ids);
    }
}
