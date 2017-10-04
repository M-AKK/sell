package com.akk.service.impl;

import com.akk.dataobject.SellerInfo;
import com.akk.repository.SellerInfoRepository;
import com.akk.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 卖家管理员登录service
 * Created by Akk_Mac
 * Date: 2017/8/30 上午9:02
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return sellerInfoRepository.findByOpenid(openid);
    }

    @Override
    public SellerInfo findSellerInfoByUsername(String username) {
        return sellerInfoRepository.findByUsername(username);
    }
}
