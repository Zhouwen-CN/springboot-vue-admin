package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;
import com.yeeiee.domain.vo.DictDataVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
public interface DictDataService extends IService<DictData> {
    List<DictDataVo> getListByTypeId(Long typeId);

    List<Long> getTypeIdByDataIds(Collection<Long> ids);

    void addDictData(DictDataForm dictDataForm);
}
