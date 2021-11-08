package com.group11.controller;

import com.group11.common.config.DiyConfig;
import com.group11.common.exception.ErrorCodeEume;
import com.group11.common.utils.R;
import com.group11.common.utils.RandomEnvelopeAmountList;
import com.group11.pojo.dto.EnvelopeWithoutOpened;
import com.group11.pojo.dto.EnvelopeWithoutUid;
import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Xu Haitong
 * @since 2021/11/3 13:31
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ApiController {
    // 全局变量
    Long globalEnvelopeId = 0L;
    Long sentAmout = 0L;
    Long sentEnvelopeCount = 0L;
    @Autowired
    DiyConfig diyConfig;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DefaultRedisScript<Long> redisScript;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/hello")
    public String hello() {
        return "服务成功启动";
    }


    @PostMapping("/snatch")
    public R snatch(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        log.info("抢红包 ==> uid: " + uid);

        if (!redisTemplate.opsForSet().isMember("uid_set", uid)) {                          // 当前 uid 不存在 uid_set 中
            if (!redisTemplate.hasKey(uid + "_uid_hash")) {                                      // 当前 uid 不存在一个 hash 对应
                return R.error(ErrorCodeEume.INVALID_UID).put("data", null);
            }
            return R.error(ErrorCodeEume.MAX_COUNT).put("data", null);                           // 当前用户已达最大抢到红包次数
        }

        Long currTime = new Date().getTime() / 1000L;
        List<String> keys = Arrays.asList(currTime.toString());
        long compareResult = redisTemplate.execute(redisScript, keys);  // lua 脚本保证原子化操作
        // -1：未抢到红包，-2：没有红包了，正数：抢到的红包返回的时间戳
        if (compareResult == -1L) {
            return R.error(ErrorCodeEume.FAILURE_SNATCH).put("data", null);
        }
        if (compareResult == -2L) {
            return R.error(ErrorCodeEume.SOLD_OUT).put("data", null);
        }
        globalEnvelopeId++;

        // 发送给消息队列
        Long randomEnvelopeValue = RandomEnvelopeAmountList.randomBonusWithSpecifyBound(
                diyConfig.getMaxAmount(), diyConfig.getMaxEnvelopeCount(), sentAmout, sentEnvelopeCount, diyConfig.getLowerLimitAmount(), diyConfig.getUpperLimitAmount());
        EnvelopeWithoutOpened envelope = new EnvelopeWithoutOpened(globalEnvelopeId, uid, randomEnvelopeValue, compareResult);
        rocketMQTemplate.convertAndSend("snatch-queue", envelope);

        if (!redisTemplate.hasKey(uid + "_uid_hash")) {  // 懒创建
            Map<String, Long> uidHashMap = new HashMap<>();
            uidHashMap.put("cur_count", 0L);
            uidHashMap.put("cur_amount", 0L);
            uidHashMap.put("finished_count", 0L);
            uidHashMap.put("finished_amount", 0L);
            redisTemplate.opsForHash().putAll(uid + "_uid_hash", uidHashMap);
        }
        Long cur_count = redisTemplate.opsForHash().increment(uid + "_uid_hash", "cur_count", 1L);  // 返回新值
        if (diyConfig.getMaxCount().equals(cur_count)) {
            redisTemplate.opsForSet().remove("uid_set", uid);
        }

        // 创建一个 envelopeId 的 hash
        Map<String, Long> envelopeIdHashMap = new HashMap<>();
        envelopeIdHashMap.put("uid", uid);
        envelopeIdHashMap.put("value", randomEnvelopeValue);
        redisTemplate.opsForHash().putAll(globalEnvelopeId + "_envelope_id_hash", envelopeIdHashMap);

        return R.ok().put("data", new SnatchResponse(globalEnvelopeId, diyConfig.getMaxCount(), cur_count));
    }


    @PostMapping("/open")
    public R open(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        long envelopeId = Long.parseLong(json.get("envelope_id"));
        log.info("开红包 ==> uid: " + uid + ", envelope_id: " + envelopeId);

        Random random = new Random();
        return R.ok().put("data", new OpenResponse((long) random.nextInt(1000)));
    }


    @PostMapping("/get_wallet_list")
    public R getWalletList(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        log.info("抢红包 ==> uid: " + uid);

        Random random = new Random();
        List<EnvelopeWithoutUid> list = new ArrayList<>();

        list.add(new EnvelopeWithoutUid(123L, (long) random.nextInt(1000), true, (long) random.nextInt(1000)));
        list.add(new EnvelopeWithoutUid(123L, null, false, (long) random.nextInt(1000)));
        return R.ok().put("data", new GetWalletListResponse((long) random.nextInt(1000), list));
    }
}
