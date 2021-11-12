package com.group11.service;

/**
 * @author Xu Haitong
 * @since 2021/11/12 10:12
 */
public interface StressTestService {
    // 单个用户所有已打开红包的金额之和
    Long getSumOfAllOpenedEnvelopeValueForAUser(Long uid);

    // 单个用户的金额
    Long getAmountOfAUser(Long uid);

    // 所有红包的金额之和
    Long getSumOfAllEnvelopeValue();
}
