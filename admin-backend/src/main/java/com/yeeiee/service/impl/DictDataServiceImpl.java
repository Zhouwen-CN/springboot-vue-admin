package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.DictData;
import com.yeeiee.entity.vo.DictDataVo;
import com.yeeiee.mapper.DictDataMapper;
import com.yeeiee.service.DictDataService;
import lombok.RequiredArgsConstructor;
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
    public List<DictDataVo> getListByType(String type) {
        return dictDataMapper.selectListByType(type);
    }
}
