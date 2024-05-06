package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.DataRange;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据范围表
 * </p>
 *
 * @author chen
 * @since 2024-04-28
 */
@RestController
@RequestMapping("/range")
@Tag(name = "数据范围表 控制器")
public class DataRangeController extends BaseController<DataRange> {

    public DataRangeController(IService<DataRange> service) {
        super(service);
    }
}

