package com.yeeiee.controller;

import com.yeeiee.entity.DataStorey;
import com.yeeiee.service.DataStoreyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数仓层级表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/storey")
@Tag(name = "数仓层级表 控制器")
public class DataStoreyController extends BaseController<DataStoreyService, DataStorey> {

    public DataStoreyController(DataStoreyService service) {
        super(service);
    }
}

