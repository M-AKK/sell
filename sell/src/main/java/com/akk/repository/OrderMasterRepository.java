package com.akk.repository;

import com.akk.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 查找总订单的DAO方法
 * Created by KHM
 * 2017/7/27 9:51
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String>{

    //按照买家的openid来查总订单(用于买家端)
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
