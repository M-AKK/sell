package com.akk.dto;

import com.akk.dataobject.OrderDetail;
import com.akk.enums.OrderStatusEnum;
import com.akk.enums.PayStatusEnum;
import com.akk.utils.EnumUtil;
import com.akk.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO类用来关联dataobject中有联系的类，比如创建订单就需要订单总表和订单详情表两种数据，
 * 所以就需要一种包含了这两种实体的包装类把他们联系起来
 * 因为用dataobject来关联的话会破坏映射的数据库的关系
 * Created by KHM
 * 2017/7/27 11:10
 */
@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL)//可以让为null属性不返回
public class OrderDTO {

    //订单id
    //@Id，不需要此注解了，因为这不是关联数据库的类
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
    private Integer orderStatus;

    //支付状态，默认为0未支付
    private Integer payStatus;

    //创建时间
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    //更新时间
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    //@Transient//为了方便关联订单总表和详情表，把此字段加在这.用此注解就可以让程序在与数据库关联时忽略此字段,但是更规范的写法就是创建新的DTO
    private List<OrderDetail> orderDetailList; //= new ArrayList<>();(配置中配置了如果为null就不返回)

    @JsonIgnore//在返回json的时候回忽略这个属性
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }

}
