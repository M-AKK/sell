package com.akk.repository;

import com.akk.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 查找商品详情的Dao方法
 * Created by KHM
 * 2017/7/26 16:28
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    //根据订单状态查询(上架或下架)
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
