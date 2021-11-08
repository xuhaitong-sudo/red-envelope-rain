package com.group11;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Xu Haitong
 * @since 2021/11/8 20:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTests {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void opsForHashIncrementTest() {
        Long t1 = redisTemplate.opsForHash().increment("a", "f1", 10);
        System.out.println(t1);
    }

    @Test
    public void hasKeyTest(){
        Boolean res = redisTemplate.hasKey("12_uid_hash");
        System.out.println(res);
    }

}
