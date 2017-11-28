package com.akk.dataobject;

import com.akk.enums.ProductStatusEnum;
import com.akk.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息表
 * Created by KHM
 * 2017/7/26 16:22
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo implements Serializable{

    private static final long serialVersionUID = -6168443724105768112L;

    @Id
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

    //状态，0正常1下架，默认在架
    private Integer productStatus = ProductStatusEnum.UP.getCode();

    //类目编号
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore//传递的时候会忽略
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }

}
