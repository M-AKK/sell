package com.akk.form;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 这是用来验证前端传过来的修改的商品表单信息
 * Created by Akk_Mac
 * Date: 2017/8/29 下午3:44
 */
@Data
public class ProductForm {

    private String productId;

    //商品名字
    private String productName;

    //单价
    private BigDecimal productPrice;

    //库存
    private Integer productStock;

    //描述
    private String productDescription;

    //小图
    private String productIcon;

    //状态，0正常1下架,这里是由上下架操作决定的，所以修改商品页面没这个操作
    //private Integer productStatus;

    //类目编号
    private Integer categoryType;
}
