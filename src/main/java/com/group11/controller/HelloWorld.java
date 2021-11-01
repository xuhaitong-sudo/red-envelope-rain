package com.group11.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xu Haitong
 * @since 2021/10/31 14:32
 */
@RestController
public class HelloWorld {

    @RequestMapping("/hello")
    public String hello() {
        return "如果你看到这条信息，说明你的容器运行成功";
    }
}
