package com.akk.enums;

import lombok.Getter;

/**
 * 商品状态
 * Created by KHM
 * 2017/7/26 16:51
 */
@Getter
public enum ProductStatusEnum implements CodeEnum {

    UP(0, "在架"),
    DOWN(1, "下架")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
