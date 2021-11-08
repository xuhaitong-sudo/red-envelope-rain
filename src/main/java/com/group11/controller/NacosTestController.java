package com.group11.controller;

import com.group11.common.config.DiyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试配置中心是否可以动态刷新配置
 * @author Xu Haitong
 * @since 2021/11/7 22:42
 */
@RestController
@RefreshScope
public class NacosTestController {

    @Autowired
    DiyConfig config;

    @RequestMapping("/test")
    public String test() {
        return config.toString();
    }
}
