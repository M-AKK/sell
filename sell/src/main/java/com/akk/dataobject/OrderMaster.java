package com.akk.dataobject;

import com.akk.enums.OrderStatusEnum;
import com.akk.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表
 * Created by KHM
 * 2017/7/27 9:35
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    //订单id
    @Id
    private String orderId;

    //买家姓名
    private String buyerName;

    //买家手机号
    private String buyerPhone;

    //买家地址
    private String buyerAddress;

    //买家微信openid
    private String buyerOpenid;

    //订单总金额
    private BigDecimal orderAmount;

    //订单状态，默认为0新下单
    private Integer orderStatus = OrderStatusEnum.New.getCode();

    //支付状态，默认为0未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;
/*
    @Transient//为了方便关联订单总表和详情表，把此字段加在哲，用此注解就可以让程序在与数据库关联时忽略此字段
    private List<OrderDetail> orderDetailList;*/


}
