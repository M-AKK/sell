package com.akk.service;

import com.akk.dto.OrderDTO;

/**
 * 买家
 * Created by KHM
 * 2017/8/1 10:15
 */
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);

}
