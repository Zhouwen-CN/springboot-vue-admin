package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.DataStorey;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数仓层级表
 * </p>
 *
 * @author chen
 * @since 2024-04-28
 */
@RestController
@RequestMapping("/dataStorey")
@Tag(name = "数仓层级表 控制器")
public class DataStoreyController extends BaseController<DataStorey> {

    public DataStoreyController(IService<DataStorey> service) {
        super(service);
    }
}

