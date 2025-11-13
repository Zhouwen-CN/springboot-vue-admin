package com.yeeiee.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.system.domain.entity.DictData;
import com.yeeiee.system.domain.entity.DictType;
import com.yeeiee.system.domain.form.DictDataForm;
import com.yeeiee.system.domain.vo.DictDataSelectorVo;

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
     *
     * @param wrapper 查询条件
     */
    List<DictDataSelectorVo> getDictByTypeId(Wrapper<DictType> wrapper);
}
