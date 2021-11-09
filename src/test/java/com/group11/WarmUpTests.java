package com.group11;

import com.group11.common.utils.Warmup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Xu Haitong
 * @since 2021/11/8 16:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WarmUpTests {
    @Autowired
    Warmup warmup;

    @Test
    public void selectUidList() {
        warmup.warmup();
    }

    @Test
    public void deleteAllKeys() {
        warmup.redisInit();
    }
}
