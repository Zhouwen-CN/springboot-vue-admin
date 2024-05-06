package com.yeeiee.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.RootWord;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 词根表
 * </p>
 *
 * @author chen
 * @since 2024-04-28
 */
@RestController
@RequestMapping("/word")
@Tag(name = "词根表 控制器")
public class RootWordController extends BaseController<RootWord> {

    public RootWordController(IService<RootWord> service) {
        super(service);
    }
}

