package com.group11.controller;

import com.group11.pojo.dto.EnvelopeWithoutOpened;
import com.group11.pojo.dto.EnvelopeWithoutOpenedAndSnatchTime;
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
    @RocketMQMessageListener(consumerGroup = "GID_snatch", topic = "snatch-queue")
    public class SnatchConsumer implements RocketMQListener<EnvelopeWithoutOpened> {

        @Override
        public void onMessage(EnvelopeWithoutOpened message) {
            log.info("SnatchConsumer ==> " + message);
            // TODO 如何保证写入数据库成功
            apiService.createEnvelope(message.getEnvelopeId(), message.getUid(), message.getValue(), message.getSnatchTime());
            redisTemplate.opsForHash().increment("u_" + message.getUid(), "finished_count", 1L);
        }
    }

    @Service
    @RocketMQMessageListener(consumerGroup = "GID_open", topic = "open-queue")
    public class OpenConsumer implements RocketMQListener<EnvelopeWithoutOpenedAndSnatchTime> {

        @Override
        public void onMessage(EnvelopeWithoutOpenedAndSnatchTime message) {
            log.info("OpenConsumer ==> " + message);
            // TODO 如何保证写入数据库成功
            apiService.openEnvelope(message.getEnvelopeId());
            apiService.updateUserAmount(message.getUid(), message.getValue());
            redisTemplate.opsForHash().increment("u_" + message.getUid(), "finished_amount", message.getValue());
        }
    }
}
