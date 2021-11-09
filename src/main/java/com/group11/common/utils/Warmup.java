package com.group11.common.utils;

import com.google.common.collect.Sets;
import com.group11.common.config.DiyConfig;
import com.group11.service.WarmUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 缓存预热
 *
 * @author Xu Haitong
 * @since 2021/11/8 15:53
 */
@Component
public class Warmup {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    WarmUpService warmUpService;

    @Autowired
    DiyConfig diyConfig;

    private Long randomSnatchTime(Long startTime) {
        Long lastTime = 600L;  // 10 分钟
        return ThreadLocalRandom.current().nextLong(startTime, startTime + lastTime + 1);
    }

    private void mySQLInit() {
        warmUpService.truncateEnvelopeTable();
        warmUpService.truncateUserTable();
        for (long i = 1; i <= 20; i++) {
            warmUpService.insertOneRowIntoEnvelopeTable(i);  // 有批量操作的写法，这里就直接循环方便阅读了
        }
    }

    // 定义 scan 方法
    private Set<String> scan(String pattern) {
        Set<String> keys = Sets.newHashSet();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build();
        Cursor<byte[]> cursor = connection.scan(scanOptions);
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        return keys;
    }

    public void redisInit() {
        // 清空 Redis 缓存
        Set<String> keys = scan("*");
        for (String key : keys) {
            redisTemplate.delete(key);
        }

        // 1. 所有 uid 存入一个 set
        List<Long> uidList = warmUpService.selectAllUsers();
        for (Long uid : uidList) {
            redisTemplate.opsForSet().add("uid_set", "u_" + uid);
        }

        // 2. 三个全局变量
        // 注意 sent_amout 是当前的，是因为 envelope_id 和 sent_envelope_count 可以通过原子化修改操作安全实现，而对 sent_amout 的修改需要加分布式锁
        redisTemplate.opsForHash().put("global_variable", "envelope_id", 0L);                         // 前一个红包 id
        redisTemplate.opsForHash().put("global_variable", "sent_envelope_count", -1L);                // 前一个已发送总红包个数
        redisTemplate.opsForHash().put("global_variable", "sent_amout", 0L);                          // 当前已发红包总金额

        // 3. 根据动态配置预先生成时间戳，然后 rpush 进 Redis 的 List 当令牌（提示：只有总金额会变化，红包个数不会发生变化）
        List<Long> snatchTimeList = new ArrayList<>();
        for (int i = 0; i < diyConfig.getMaxEnvelopeCount(); i++) {
            snatchTimeList.add(randomSnatchTime(diyConfig.getStartTime()));
        }
        Collections.sort(snatchTimeList);
        for (Long snatchTime : snatchTimeList) {
            redisTemplate.opsForList().rightPush("snatch_time_bucket", snatchTime);
        }
    }

    @PostConstruct  // 使用 @PostConstruct 在 web 服务启动前进行预热
    public void warmup() {
        mySQLInit();
        redisInit();
    }
}
