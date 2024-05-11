package com.yeeiee.controller;

import com.yeeiee.entity.RootWord;
import com.yeeiee.service.RootWordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 词根表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/word")
@Tag(name = "词根表 控制器")
public class RootWordController extends BaseController<RootWordService, RootWord> {

    public RootWordController(RootWordService service) {
        super(service);
    }
}

