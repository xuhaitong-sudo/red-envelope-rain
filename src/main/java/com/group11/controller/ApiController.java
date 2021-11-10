package com.group11.controller;

import com.alibaba.fastjson.JSONObject;
import com.group11.common.config.DiyConfig;
import com.group11.common.exception.ErrorCodeEume;
import com.group11.common.limit.AccessLimit;
import com.group11.common.utils.R;
import com.group11.common.utils.RandomEnvelopeAmountList;
import com.group11.pojo.dto.EnvelopeWithoutOpened;
import com.group11.pojo.dto.EnvelopeWithoutOpenedAndSnatchTime;
import com.group11.pojo.dto.EnvelopeWithoutUid;
import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;
import com.group11.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    @Autowired
    private DiyConfig diyConfig;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DefaultRedisScript<Long> redisScript;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ApiService apiService;

    @AccessLimit(maxCount = 2, seconds = 5)  // 对这个方法进行限流，默认单个 ip 每秒只能最多访问 5 次
    @GetMapping("/hello")
    public String hello() {
        return "服务成功启动";
    }

    @AccessLimit
    @PostMapping("/snatch")
    public R snatch(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        log.info("抢红包 ==> uid: " + uid);

        if (!redisTemplate.opsForSet().isMember("uid_set", "u_" + uid)) {                          // 当前 uid 不存在 uid_set 中
            if (!redisTemplate.hasKey("u_" + uid)) {                                             // 当前 uid 不存在一个 hash 对应
                return R.error(ErrorCodeEume.INVALID_UID).put("data", null);
            }
            return R.error(ErrorCodeEume.MAX_COUNT).put("data", null);                           // 当前用户已达最大抢到红包次数
        }

        Random random = new Random();
        if (random.nextInt(100) + 1 > 100 * diyConfig.getProbability()) {
            return R.error(ErrorCodeEume.FAILURE_SNATCH).put("data", null);
        }

        long currTime = new Date().getTime() / 1000L;
        List<String> keys = Arrays.asList(String.valueOf(currTime));
        long compareResult = redisTemplate.execute(redisScript, keys);  // lua 脚本保证原子化操作
        // -1：未抢到红包，-2：没有红包了，正数：抢到的红包返回的时间戳
        if (compareResult == -1L) {
            return R.error(ErrorCodeEume.FAILURE_SNATCH).put("data", null);
        }
        if (compareResult == -2L) {
            return R.error(ErrorCodeEume.SOLD_OUT).put("data", null);
        }

        Long enveLopeId = redisTemplate.opsForHash().increment("global_variable", "envelope_id", 1);
        Long sentEnvelopeCount = redisTemplate.opsForHash().increment("global_variable", "sent_envelope_count", 1);

        RLock lock = redissonClient.getLock("lock");  // 分布式锁
        lock.lock();
        Integer sentAmout = (Integer) redisTemplate.opsForHash().get("global_variable", "sent_amout");  // TODO Long 和 Integer
        Long value = RandomEnvelopeAmountList.randomBonusWithSpecifyBound(
                diyConfig.getMaxAmount(), diyConfig.getMaxEnvelopeCount(), Long.valueOf(sentAmout.toString()), sentEnvelopeCount, diyConfig.getLowerLimitAmount(), diyConfig.getUpperLimitAmount());
        redisTemplate.opsForHash().increment("global_variable", "sent_amout", value);
        lock.unlock();
        EnvelopeWithoutOpened envelope = new EnvelopeWithoutOpened(enveLopeId, uid, value, compareResult);

        rocketMQTemplate.convertAndSend("snatch-queue", envelope);

        if (!redisTemplate.hasKey("u_" + uid)) {  // 懒创建
            Map<String, Long> uidHashMap = new HashMap<>();
            uidHashMap.put("cur_count", 0L);
            uidHashMap.put("cur_amount", 0L);
            uidHashMap.put("finished_count", 0L);
            uidHashMap.put("finished_amount", 0L);
            redisTemplate.opsForHash().putAll("u_" + uid, uidHashMap);
        }
        Long curCount = redisTemplate.opsForHash().increment("u_" + uid, "cur_count", 1L);  // 返回新值
        if (diyConfig.getMaxCount().equals(curCount)) {
            redisTemplate.opsForSet().remove("uid_set", "u_" + uid);
        }

        // 创建一个 envelopeId 的 hash
        Map<String, Long> envelopeIdHashMap = new HashMap<>();
        envelopeIdHashMap.put("uid", uid);
        envelopeIdHashMap.put("value", value);
        redisTemplate.opsForHash().putAll("e_" + enveLopeId, envelopeIdHashMap);

        return R.ok().put("data", new SnatchResponse(enveLopeId, diyConfig.getMaxCount(), curCount));
    }

    @AccessLimit
    @PostMapping("/open")
    public R open(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        long envelopeId = Long.parseLong(json.get("envelope_id"));
        log.info("开红包 ==> uid: " + uid + ", envelope_id: " + envelopeId);

        // TODO Long 和 Integer
        if (!redisTemplate.hasKey("e_" + envelopeId) || !(uid == (Integer) redisTemplate.opsForHash().get("e_" + envelopeId, "uid"))) {
            return R.error(ErrorCodeEume.ENVELOPE_NOT_EXIST).put("data", null);
        }
        // TODO Long 和 Integer
        Integer value = (int) redisTemplate.opsForHash().get("e_" + envelopeId, "value");
        redisTemplate.opsForHash().increment("u_" + uid, "cur_amount", value);
        redisTemplate.delete("e_" + envelopeId);

        rocketMQTemplate.convertAndSend("open-queue", new EnvelopeWithoutOpenedAndSnatchTime(envelopeId, uid, Long.parseLong(value.toString())));

        return R.ok().put("data", new OpenResponse(Long.parseLong(value.toString())));
    }

    @AccessLimit
    @PostMapping("/get_wallet_list")
    public R getWalletList(@RequestBody Map<String, String> json) {
        long uid = Long.parseLong(json.get("uid"));
        log.info("查询余额和红包列表 ==> uid: " + uid);

        if (!redisTemplate.hasKey("u_" + uid)) {
            if (!redisTemplate.opsForSet().isMember("uid_set", "u_" + uid)) {
                return R.error(ErrorCodeEume.INVALID_UID).put("data", new GetWalletListResponse(0L, null));
            }
            return R.ok().put("data", new GetWalletListResponse(0L, null));
        }

        // TODO Integr 和 Long
        Integer curCount = (Integer) redisTemplate.opsForHash().get("u_" + uid, "cur_count");
        Integer curAmount = (Integer) redisTemplate.opsForHash().get("u_" + uid, "cur_amount");
        Integer finishedCount = (Integer) redisTemplate.opsForHash().get("u_" + uid, "finished_count");
        Integer finishedAmount = (Integer) redisTemplate.opsForHash().get("u_" + uid, "finished_amount");

        if (!curCount.equals(finishedCount) || !curAmount.equals(finishedAmount)) {
            return R.error(ErrorCodeEume.SYSTEM_BUSY).put("data", new GetWalletListResponse(0L, null));
        }

        if (redisTemplate.hasKey("ue_" + uid) && finishedCount + finishedAmount == (Integer) redisTemplate.opsForHash().get("ue_" + uid, "check")) {  // 直接返回缓存
            return R.ok().put("data", redisTemplate.opsForHash().get("ue_" + uid, "cached_result"));
        }

        // 去数据库查
        List<EnvelopeWithoutUid> envelopeList = apiService.selectEnvelopes(uid);

        // 写入缓存（这里我们手动写入缓存，其实可以引入 SpringCache 加一个注解自动实现缓存）
        Map<String, Object> data = new HashMap<>();
        data.put("amount", curAmount);
        data.put("envelope_list", envelopeList);
        redisTemplate.opsForHash().put("ue_" + uid, "check", finishedCount + finishedAmount);
        redisTemplate.opsForHash().put("ue_" + uid, "cached_result", new JSONObject(data));

        return R.ok().put("data", new GetWalletListResponse(curAmount.longValue(), envelopeList));
    }
}
