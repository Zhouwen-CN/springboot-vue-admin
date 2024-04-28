package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.DataField;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据域表
 * </p>
 *
 * @author chen
 * @since 2024-04-28
 */
@RestController
@RequestMapping("/dataField")
@Tag(name = "数据域表 控制器")
public class DataFieldController extends BaseController<DataField> {

    public DataFieldController(IService<DataField> service) {
        super(service);
    }
}

