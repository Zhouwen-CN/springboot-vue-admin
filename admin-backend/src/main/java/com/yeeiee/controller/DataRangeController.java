package com.yeeiee.controller;

import com.yeeiee.entity.DataRange;
import com.yeeiee.service.DataRangeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据范围表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/range")
@Tag(name = "数据范围表 控制器")
public class DataRangeController extends BaseController<DataRangeService, DataRange> {

    public DataRangeController(DataRangeService service) {
        super(service);
    }
}

