package com.yeeiee.controller;

import com.yeeiee.entity.DataField;
import com.yeeiee.service.DataFieldService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据域表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/field")
@Tag(name = "数据域表 控制器")
public class DataFieldController extends BaseController<DataFieldService, DataField> {

    public DataFieldController(DataFieldService service) {
        super(service);
    }
}

