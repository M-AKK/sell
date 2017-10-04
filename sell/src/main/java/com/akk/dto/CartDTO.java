package com.akk.dto;

import lombok.Data;

/**
 * 购物车
 * Created by KHM
 * 2017/7/29 10:03
 */
@Data
public class CartDTO {

    //商品Id
    private String productId;
    //数量
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
