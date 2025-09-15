package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.DictData;
import com.yeeiee.domain.form.DictDataForm;

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
}
