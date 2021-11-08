package com.group11.service.impl;

import com.group11.dao.ApiDao;
import com.group11.pojo.dto.EnvelopeWithoutUid;
import com.group11.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:58
 */
@Service
public class APiServiceImpl implements ApiService {

    @Autowired
    ApiDao apiDao;

    @Override
    public int createEnvelope(Long envelopeId, Long uid, Long value, Long snatchTime) {
        return apiDao.createEnvelope(envelopeId, uid, value, snatchTime);
    }

    @Override
    public int openEnvelope(Long envelopeId) {
        return apiDao.openEnvelope(envelopeId);
    }

    @Override
    public int updateUserAmount(Long uid, Long value) {
        return apiDao.updateUserAmount(uid, value);
    }

    @Override
    public Long selectUserAmout(Long uid) {
        return apiDao.selectUserAmout(uid);
    }

    @Override
    public List<EnvelopeWithoutUid> selectEnvelopes(Long uid) {
        return apiDao.selectEnvelopes(uid);
    }
}
