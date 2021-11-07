package com.group11;

import com.group11.common.config.DiyConfig;
import com.group11.common.utils.RandomEnvelopeAmountList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedEnvelopeDemoApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    DiyConfig config;

    @Test
    void mySQLConnectionTest() throws SQLException {
        // 测试 MySQL 是否连接成功
        System.out.println("数据源: " + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("连接: " + connection);
        System.out.println("连接地址: " + connection.getMetaData().getURL());
        connection.close();
    }

    @Test
    void redisConnectionTest() {
        // 测试 Redis 是否连接成功
        redisTemplate.opsForValue().set("group", 11);
        redisTemplate.opsForValue().set("group", 11, 100, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("group"));
    }

    @Test
    void randomEnvelopeAmountTest() {
        for (int i = 0; i < 10; i++) {
            List<Long> bonusList = RandomEnvelopeAmountList.createAmountList(config.getMaxAmount(), config.getMaxEnvelopeCount(), config.getLowerLimitAmount(), config.getUpperLimitAmount());
            System.out.println("第 " + i + "次");
            int s = 0;
            for (long x : bonusList) {
                s += x;
            }
            System.out.println("和为 " + s);
        }
    }
}
