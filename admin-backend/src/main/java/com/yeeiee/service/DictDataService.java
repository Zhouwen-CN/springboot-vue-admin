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
    /**
     * 根据字典类型id查询字典数据
     *
     * @param typeId 字典类型 id
     * @return 字典数据列表
     */
    List<DictDataVo> getListByTypeId(Long typeId);

    /**
     * 根据字典数据 id 列表，查询字典类型 id 列表
     *
     * @param ids 字典数据 id 列表
     * @return 字典类型 id 列表
     */
    List<Long> getTypeIdByDataIds(Collection<Long> ids);

    /**
     * 添加字典数据
     * @param dictDataForm 字典数据表单
     */
    void addDictData(DictDataForm dictDataForm);
}
