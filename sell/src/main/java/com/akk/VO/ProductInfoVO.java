package com.akk.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 需要返回的商品详情
 * Created by KHM
 * 2017/7/26 17:54
 */
@Data
public class ProductInfoVO implements Serializable {

    private static final long serialVersionUID = -3013889380494680036L;

    //为了防止多个name造成混淆，所以要细起名，但为了和返回对象名一致，所以用这个注解
    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;

}
