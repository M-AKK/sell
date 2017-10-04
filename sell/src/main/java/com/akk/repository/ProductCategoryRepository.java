package com.akk.repository;

import com.akk.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by KHM
 * 2017/7/26 9:35
 * Repository相当于mapper类
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{

    //传入Integer类型的CategoryType列来查找类目商品列
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
