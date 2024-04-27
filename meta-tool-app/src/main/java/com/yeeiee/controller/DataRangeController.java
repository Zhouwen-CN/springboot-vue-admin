package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.DataRange;
import com.yeeiee.mapper.DataRangeMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据范围表
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@RestController
@RequestMapping("/range")
@Tag(name = "数据范围表 控制器")
public class DataRangeController extends BaseController<DataRangeMapper, DataRange> {

    public DataRangeController(ServiceImpl<DataRangeMapper, DataRange> service) {
        super(service);
    }
}

