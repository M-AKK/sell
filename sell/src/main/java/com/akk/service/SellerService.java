package com.akk.service;

import com.akk.dataobject.SellerInfo;

/**
 * Created by Akk_Mac
 * Date: 2017/8/30 上午9:01
 */
public interface SellerService {

    SellerInfo findSellerInfoByOpenid(String openid);

    SellerInfo findSellerInfoByUsername(String username);
}
