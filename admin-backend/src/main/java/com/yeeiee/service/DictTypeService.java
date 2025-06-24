package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.DictType;
import com.yeeiee.domain.form.DictTypeForm;

import java.util.Collection;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-02-13
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 新增字典类型
     * @param dictTypeForm 字典类型表单
     */
    void addDictType(DictTypeForm dictTypeForm);

    /**
     * 根据id列表批量删除字典类型
     * @param ids 字典类型id列表
     */
    void removeDictTypeByIds(Collection<Long> ids);
}
