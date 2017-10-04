package com.akk.service;

import com.akk.dataobject.ProductCategory;

import java.util.List;

/**
 * Created by KHM
 * 2017/7/26 10:50
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    //根据类目类型来查找商品
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);


}
