package com.group11;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class RedEnvelopeDemoApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    RedisTemplate redisTemplate;

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
        System.out.println(redisTemplate.opsForValue().get("group"));
    }

}
