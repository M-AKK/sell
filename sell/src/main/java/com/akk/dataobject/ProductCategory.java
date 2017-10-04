package com.akk.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目
 * Created by KHM
 * 2017/7/26 9:24
 * 写了这个发现是可以先建立数据库，然后根据数据库来完成实体代码
 * 只要加上合适的注解就行
 */
@Entity//很关键，让代码和数据库对接
@DynamicUpdate//动态更新，只要数据发生变化了，数据库的时间也会变
@Data//包含生成get\set\toString的方法
public class ProductCategory {

    /* 类目id. */
    @Id
    @GeneratedValue
    private Integer categoryId;

    //类目名字
    private String categoryName;

    //类目编号
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    //在进行某些查询方法时要用得到
    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
