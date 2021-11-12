package com.group11.controller;

import com.group11.common.config.DiyConfig;
import com.group11.service.StressTestService;
import com.group11.service.WarmUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试配置中心是否可以动态刷新配置
 *
 * @author Xu Haitong
 * @since 2021/11/7 22:42
 */
@RestController
@RefreshScope
public class TestController {
    @Autowired
    DiyConfig config;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    StressTestService stressTestService;
    @Autowired
    WarmUpService warmUpService;

    @RequestMapping("/nacos-test")
    public String nacosTest() {
        return config.toString();
    }

    @RequestMapping("/stress-test")
    public String stressTest() {
        // TODO Long 和 Integer
        Integer sentAmount = (Integer) redisTemplate.opsForHash().get("global_variable", "sent_amout");
        Long sumOfAllEnvelopeValue = stressTestService.getSumOfAllEnvelopeValue();
        if (!(sumOfAllEnvelopeValue.equals(new Long(sentAmount)))) {
            return "已发出的红包总金额和数据库不一致";
        }
        List<Long> uids = warmUpService.selectAllUsers();
        for (Long uid : uids) {
            Long amountOfAUser = stressTestService.getAmountOfAUser(uid);
            Long sumOfAllOpenedEnvelopeValueForAUser = stressTestService.getSumOfAllOpenedEnvelopeValueForAUser(uid);
            if (sumOfAllOpenedEnvelopeValueForAUser != null && !amountOfAUser.equals(sumOfAllOpenedEnvelopeValueForAUser)) {
                return "uid 为 " + uid + " 的已打开红包和用户金额不一致";
            }
        }
        return "压测数据表现正常";
    }
}
