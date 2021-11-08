package com.group11.service;

import com.group11.pojo.dto.Envelope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:37
 */
public interface ApiService {

    // 抢红包：在红包表中创建一个未打开的红包
    int createEnvelope(Long envelopeId, Long uid, Long value, Long snatchTime);

    // 开红包：将红包表中 envelopeId 对应记录的 opened 从 0 改成 1
    int openEnvelope(Long envelopeId);

    // 开红包：在用户表 uid 对应记录的 amount 成 amount + value
    int updateUserAmount(@Param("uid") Long uid, @Param("value") Long value);

    // 查询总金额和红包列表：查询用户表中的 uid 对应记录的 amount
    Long selectUserAmout(@Param("uid") Long uid);

    // 查询总金额和红包列表：查询红包表中 uid 对应的所有红包
    List<Envelope> selectEnvelopes(@Param("uid") Long uid);
}
