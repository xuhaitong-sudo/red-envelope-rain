package com.group11.dao;

import com.group11.pojo.dto.Envelope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/3 16:07
 */
@Repository
@Mapper
public interface ApiDao {
    int createEnvelope(@Param("envelopeId") Long envelopeId, @Param("uid") Long uid, @Param("value") Long value, @Param("snatchTime") Long snatchTime);

    int openEnvelope(@Param("envelopeId") Long envelopeId);

    int updateUserAmount(@Param("uid") Long uid, @Param("value") Long value);

    Long selectUserAmout(@Param("uid") Long uid);

    List<Envelope> selectEnvelopes(@Param("uid") Long uid);
}
