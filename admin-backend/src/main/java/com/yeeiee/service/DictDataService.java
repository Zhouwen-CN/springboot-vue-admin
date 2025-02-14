package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.DictData;
import com.yeeiee.entity.vo.DictDataVo;
import org.apache.ibatis.annotations.Param;

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
    List<DictDataVo> getListByType(@Param("type") String type);
}
