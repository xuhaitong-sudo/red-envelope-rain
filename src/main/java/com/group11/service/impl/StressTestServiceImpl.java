package com.group11.service.impl;

import com.group11.dao.StressTestDao;
import com.group11.service.StressTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Xu Haitong
 * @since 2021/11/12 10:14
 */
@Service
public class StressTestServiceImpl implements StressTestService {
    @Autowired
    StressTestDao stressTestDao;

    @Override
    public Long getSumOfAllOpenedEnvelopeValueForAUser(Long uid) {
        return stressTestDao.getSumOfAllOpenedEnvelopeValueForAUser(uid);
    }

    @Override
    public Long getAmountOfAUser(Long uid) {
        return stressTestDao.getAmountOfAUser(uid);
    }

    @Override
    public Long getSumOfAllEnvelopeValue() {
        return stressTestDao.getSumOfAllEnvelopeValue();
    }
}
