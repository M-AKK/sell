package com.akk.service;


import com.akk.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

/**
 * 支付
 * Created by Akk_Mac
 * Date: 2017/8/25 下午4:13
 */
public interface PayService {

    //创建支付
    PayResponse create(OrderDTO orderDTO);

    //支付回调验证，告诉微信服务器已经支付成功
    PayResponse notify(String notifyData);

    //退款
    RefundResponse refund(OrderDTO orderDTO);

}
