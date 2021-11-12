package com.group11.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 压力测试
 *
 * @author Xu Haitong
 * @since 2021/11/12 9:59
 */
@Repository
@Mapper
public interface StressTestDao {
    Long getSumOfAllOpenedEnvelopeValueForAUser(@Param("uid") Long uid);

    Long getAmountOfAUser(@Param("uid") Long uid);

    Long getSumOfAllEnvelopeValue();
}
