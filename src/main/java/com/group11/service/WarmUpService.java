package com.group11.service;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/8 16:04
 */
public interface WarmUpService {
    // 查询所有 uid
    List<Long> selectAllUsers();

    int truncateUserTable();

    int truncateEnvelopeTable();

    int insertOneRowIntoEnvelopeTable(Long uid);
}
