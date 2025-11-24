package com.yeeiee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.common.exception.DmlOperationException;
import com.yeeiee.common.utils.BeanUtil;
import com.yeeiee.system.domain.entity.DictData;
import com.yeeiee.system.domain.entity.DictType;
import com.yeeiee.system.domain.form.DictDataForm;
import com.yeeiee.system.domain.vo.DictDataSelectorVo;
import com.yeeiee.system.mapper.DictDataMapper;
import com.yeeiee.system.service.DictDataService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

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
    public void addDictData(DictDataForm dictDataForm) {
        val exists = this.exists(
                Wrappers.<DictData>lambdaQuery()
                        .eq(DictData::getTypeId, dictDataForm.getTypeId())
                        .and(c1 -> c1.eq(DictData::getLabel, dictDataForm.getLabel())
                                .or(c2 -> c2.eq(DictData::getData, dictDataForm.getData()))
                        )
        );

        if (exists) {
            throw new DmlOperationException("标签键或标签值已存在");
        }

        val dictData = BeanUtil.toBean(dictDataForm, DictData.class);
        this.save(dictData);
    }

    @Override
    public List<DictDataSelectorVo> getDictByTypeId(Wrapper<DictType> wrapper) {
        return dictDataMapper.selectDictByTypeId(wrapper);
    }
}
