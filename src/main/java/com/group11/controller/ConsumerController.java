package com.group11.controller;

import com.group11.pojo.dto.EnvelopeWithoutOpened;
import com.group11.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 通过消息队列接收抢红包接口和开红包接口消息真正向 MySQL 写入数据的消费者
 *
 * @author Xu Haitong
 * @since 2021/11/8 20:59
 */
@Component
@Slf4j
public class ConsumerController {
    @Autowired
    ApiService apiService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Service
    @RocketMQMessageListener(consumerGroup = "group11", topic = "snatch-queue")
    public class SnatchConsumer implements RocketMQListener<EnvelopeWithoutOpened> {

        @Override
        public void onMessage(EnvelopeWithoutOpened message) {
            log.info("SnatchConsumer ==> " + message);
            apiService.createEnvelope(message.getEnvelopeId(), message.getUid(), message.getValue(), message.getSnatchTime());
            redisTemplate.opsForHash().increment(message.getUid() + "_uid_hash", "finished_count", 1L);
        }
    }
}
