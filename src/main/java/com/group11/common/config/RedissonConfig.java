package com.group11.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 实现分布式锁
 *
 * @author Xu Haitong
 * @since 2021/11/9 20:28
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort)
                .setSubscriptionsPerConnection(10)
//                .setPassword("Hongbaoyugroup11")
                .setSubscriptionConnectionPoolSize(100)
                .setPingConnectionInterval(1000);
        return Redisson.create(config);
    }
}
