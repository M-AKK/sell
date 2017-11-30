package com.akk.service.impl;

import com.akk.dto.OrderDTO;
import com.akk.enums.ResultEnum;
import com.akk.exception.SellException;
import com.akk.service.OrderService;
import com.akk.service.PayService;
import com.akk.utils.JsonUtil;
import com.akk.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Akk_Mac
 * Date: 2017/8/25 下午4:27
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService{

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        //发起支付需要传一些参数
        payRequest.setOpenid(orderDTO.getBuyerOpenid());//用户openid
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());//订单总金额
        payRequest.setOrderId(orderDTO.getOrderId());//订单orderid
        payRequest.setOrderName(ORDER_NAME);//订单名字，自己随便起
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);//支付方式
        log.info("【微信支付】发起支付，request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);//根据传参得到预付支付的参数
        log.info("【微信支付】发起支付生成预付信息，response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //1. 验证签名
        //2. 支付状态
        //3. 支付金额
        //4. 支付人(下单人 == 支付人)比如有代付，有的必须本人支付

        //前两步SDK已经做了

        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，payResponse={}", JsonUtil.toJson(payResponse));

        //查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());
        //判断订单是否存在
        if(orderDTO == null) {
            log.info("【微信支付】异步通知，订单不存在，orderId={}", payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断金额是否一致,由于这里两个变量的类型不一致，数据库用的BigDecimal而SDK用的double,所以要做转换
        //但是转换类型比较还是不行，因为转换后小数点后面会出现奇怪的数字，所以要用相减判断
        if(!MathUtil.equals(payResponse.getOrderAmount(), orderDTO.getOrderAmount().doubleValue())) {
            log.info("【微信支付】异步通知，订单金额不一致，orderId={}，微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }
        //修改订单支付状态
        orderService.paid(orderDTO);

        return payResponse;
    }

    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】request={}", JsonUtil.toJson(refundRequest));

        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】response={}", JsonUtil.toJson(refundResponse));

        return refundResponse;
    }
}
