package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.DataField;
import com.yeeiee.mapper.DataFieldMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据域表
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@RestController
@RequestMapping("/field")
@Tag(name = "数据域表 控制器")
public class DataFieldController extends BaseController<DataFieldMapper, DataField> {

    public DataFieldController(ServiceImpl<DataFieldMapper, DataField> service) {
        super(service);
    }
}

