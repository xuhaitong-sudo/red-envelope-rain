package com.group11.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 红包雨动态配置
 *
 * @author Xu Haitong
 * @since 2021/11/3 13:52
 */
@Component
@ConfigurationProperties(prefix = "diyconfig")
@Data
@RefreshScope
public class DiyConfig {
    private Long maxCount;           // 单个用户抢到红包个数上限
    private Double probability;      // 用户抢红包概率
    private Long maxAmount;          // 红包总金额（动态变化）
    private Long maxEnvelopeCount;   // 红包总个数
    private Long lowerLimitAmount;   // 红包金额下限
    private Long upperLimitAmount;   // 红包金额上限
    private Long startTime;          // 活动开始时间（额外添加的一个参数）
}
