package com.akk.controller;

import com.akk.dataobject.ProductCategory;
import com.akk.dataobject.ProductInfo;
import com.akk.exception.SellException;
import com.akk.form.ProductForm;
import com.akk.service.CategoryService;
import com.akk.service.ProductService;
import com.akk.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卖家端商品
 * Created by Akk_Mac
 * Date: 2017/8/27 下午4:21
 */
@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")//这里给了默认的页码，第一页
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map) {
        PageRequest pageRequest = new PageRequest(page - 1, size);//起始页码不一样，要减一才能和方法对住
        Page<ProductInfo> productInfoPage = productService.findAll(pageRequest);
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);

        return new ModelAndView("product/list", map);
    }

    /**
     * 商品上架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/on_sale")
    //每次更新的时候把缓存也更新,但这个cacheput要求返回的对象要序列化，但这里的ModelAndView不能序列化
    //@CachePut(cacheNames = "product", key = "123")
    //所以用这个，清除缓存，访问这个方法之后，会把缓存清除掉，然后用户在调用列表就会重新查找数据库了
    @CacheEvict(cacheNames = "product", key = "123")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String, Object> map) {
        try {
            productService.onSale(productId);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    /**
     * 商品下架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/off_sale")
    //每次更新的时候把缓存也更新,但这个cacheput要求返回的对象要序列化，但这里的ModelAndView不能序列化
    //@CachePut(cacheNames = "product", key = "123")
    //所以用这个，清除缓存，访问这个方法之后，会把缓存清除掉，然后用户在调用列表就会重新查找数据库了
    @CacheEvict(cacheNames = "product", key = "123")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                                Map<String, Object> map) {
        try {
            productService.offSale(productId);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    /**
     *类目新增页面
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/index")//required是不用必填
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                              Map<String, Object> map) {
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        }

        //查询所有类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("product/index", map);
    }

    /**
     * 新增/修改
     *
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    //每次更新的时候把缓存也更新,但这个cacheput要求返回的对象要序列化，但这里的ModelAndView不能序列化
    //@CachePut(cacheNames = "product", key = "123")
    //所以用这个，清除缓存，访问这个方法之后，会把缓存清除掉，然后用户在调用列表就会重新查找数据库了
    @CacheEvict(cacheNames = "product", key = "123")
    public ModelAndView save(@Valid ProductForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        //如果传过来的参数有错误，就跳转错误页面
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        ProductInfo productInfo = new ProductInfo();

        try {
            //如果productid是null说明为新增商品
            if (!StringUtils.isEmpty(form.getProductId())) {
                //现在是修改，由于上、下架状态是前端传不过来的属性，所以要先查找一次再copy
                productInfo = productService.findOne(form.getProductId());
            } else {
                //设置下商品Id
                form.setProductId(KeyUtil.genUniqueKey());
            }

            BeanUtils.copyProperties(form, productInfo);
            //进行存储操作
            productService.save(productInfo);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }
}
