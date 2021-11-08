package com.group11.service.impl;

import com.group11.dao.WarmUpDao;
import com.group11.service.WarmUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xu Haitong
 * @since 2021/11/8 16:05
 */
@Service
public class WarmUpServiceImpl implements WarmUpService {
    @Autowired
    WarmUpDao warmUpDao;

    @Override
    public List<Long> selectAllUsers() {
        return warmUpDao.selectAllUsers();
    }
}
