package com.group11.service;

import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;

/**
 * @author Xu Haitong
 * @since 2021/11/3 15:37
 */
public interface Service {

    SnatchResponse snatch(Long uid);

    OpenResponse open(Long uid, Long envelopeId);

    GetWalletListResponse getWalletList(Long uid);

}
