package com.akk.repository;

import com.akk.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 卖家登录的Dao
 * Created by Akk_Mac
 * Date: 2017/8/30 上午8:31
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String>{

    //扫码登录可以用这个查
    SellerInfo findByOpenid(String openid);

    //普通登录还是用用户名
    SellerInfo findByUsername(String username);

}
