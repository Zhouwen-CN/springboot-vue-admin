package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;
import com.yeeiee.domain.vo.DictDataVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.DictDataMapper;
import com.yeeiee.service.DictDataService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
@RequiredArgsConstructor
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final DictDataMapper dictDataMapper;

    @Override
    public List<DictDataVo> getListByTypeId(Long typeId) {
        return dictDataMapper.selectListByTypeId(typeId);
    }

    @Override
    public List<Long> getTypeIdByDataIds(Collection<Long> ids) {
        return dictDataMapper.selectTypeIdByDataIds(ids);
    }

    @Override
    public void addDictData(DictDataForm dictDataForm) {
        val exists = this.exists(
                Wrappers.<DictData>lambdaQuery()
                        .eq(DictData::getTypeId, dictDataForm.getTypeId())
                        .and(c1 -> c1.eq(DictData::getLabel, dictDataForm.getLabel())
                                .or(c2 -> c2.eq(DictData::getValue, dictDataForm.getValue()))
                        )
        );

        if (exists) {
            throw new DmlOperationException("标签键或标签值已存在");
        }

        this.save(dictDataForm.toBean());
    }
}
