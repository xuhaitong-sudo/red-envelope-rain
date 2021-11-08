package com.group11;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/8 19:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaScriptTests {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    DefaultRedisScript<Long> redisScript;

    @Test
    public void executeLuaScriptTest() {
        List<String> keys = Arrays.asList("20");
        Long result = redisTemplate.execute(redisScript, keys);
        System.out.println(result);
    }

}
