package com.group11;

import com.group11.common.config.DiyConfig;
import com.group11.pojo.dto.EnvelopeWithoutUid;
import com.group11.service.ApiService;
import com.group11.service.WarmUpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedEnvelopeDemoApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    DiyConfig config;
    @Autowired
    ApiService apiService;
    @Autowired
    WarmUpService warmUpService;

    @Test
    public void mySQLConnectionTest() throws SQLException {
        // 测试 MySQL 是否连接成功
        System.out.println("数据源: " + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("连接: " + connection);
        System.out.println("连接地址: " + connection.getMetaData().getURL());
        connection.close();
    }

    @Test
    public void redisConnectionTest() {
        // 测试 Redis 是否连接成功
        redisTemplate.opsForValue().set("group", 11);
        redisTemplate.opsForValue().set("group", 11, 100, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("group"));
    }

    @Test
    public void apiServiceTest() {
        // 测试 apiMapper，增删改返回影响了多少行
//        int t1 = mapper.createEnvelope(1L, 100L, 2L, 1000L);
//        System.out.println("t1 " + t1);
//        int t2 = mapper.openEnvelope(1L);
//        System.out.println("t2 " + t2);
//        int t3 = mapper.updateUserAmount(1L, 30L);
//        System.out.println("t3 " + t3);
        Long t4 = apiService.selectUserAmout(1L);
        System.out.println(t4);
        List<EnvelopeWithoutUid> envelopeWithoutUidList = apiService.selectEnvelopes(1L);
        for (EnvelopeWithoutUid envelopeWithoutUid : envelopeWithoutUidList) {
            System.out.println(envelopeWithoutUid);
        }
    }

    @Test
    public void warmUpServiceTest() {
        // 测试 warmUpMapper
        List<Long> t = warmUpService.selectAllUsers();
        for (Long aLong : t) {
            System.out.println(aLong);
        }
    }
}
