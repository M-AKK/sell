package com.akk.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品(包含类目)
 * Created by KHM
 * 2017/7/26 17:49
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 6332647234005804842L;

    @JsonProperty("name")//为了防止多个name造成混淆，所以要细起名，但为了和返回对象名一致，所以用这个注解
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
