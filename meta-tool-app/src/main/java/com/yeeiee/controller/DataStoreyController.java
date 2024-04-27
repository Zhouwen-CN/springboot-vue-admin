package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.DataStorey;
import com.yeeiee.mapper.DataStoreyMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数仓层级表
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@RestController
@RequestMapping("/storey")
@Tag(name = "数仓层级表 控制器")
public class DataStoreyController extends BaseController<DataStoreyMapper, DataStorey> {

    public DataStoreyController(ServiceImpl<DataStoreyMapper, DataStorey> service) {
        super(service);
    }
}

