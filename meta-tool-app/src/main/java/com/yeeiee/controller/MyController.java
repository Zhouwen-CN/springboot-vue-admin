package com.yeeiee.controller;

import com.yeeiee.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping("/hello")
    public R<String> hello() {
        int i = 1 / 0;
        return R.ok("hellow world");
    }
}
