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
    private Long maxCount;
    private Double probability;
    private Long maxAmount;
    private Long maxEnvelopeCount;
    private Long lowerLimitAmount;
    private Long upperLimitAmount;
}
