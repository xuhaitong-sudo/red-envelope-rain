package com.group11.controller;

import com.group11.common.utils.Warmup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xu Haitong
 * @since 2021/11/12 14:55
 */
@RestController
@Slf4j
public class WarmUpController {

    @Autowired
    Warmup warmup;

    @RequestMapping("/clear-and-warm-up")
    public String warmUp() {
        log.info("清除之前的数据并进行预热");
        warmup.warmup();
        return "预热成功\n";
    }
}
