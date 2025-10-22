package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;
import com.yeeiee.domain.vo.DictDataSelectorVo;

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
    /**
     * 添加字典数据
     * @param dictDataForm 字典数据表单
     */
    void addDictData(DictDataForm dictDataForm);

    /**
     * 根据字典类型id查询字典数据列表
     * @param typeId 字典类型id
     */
    List<DictDataSelectorVo> getDictByTypeId(Long typeId);
}
