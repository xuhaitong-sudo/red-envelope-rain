package com.group11.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/8 15:57
 */
@Repository
@Mapper
public interface WarmUpDao {
    List<Long> selectAllUsers();

    int truncateUserTable();

    int truncateEnvelopeTable();

    int insertOneRowIntoEnvelopeTable(@Param("uid")Long uid);
}
