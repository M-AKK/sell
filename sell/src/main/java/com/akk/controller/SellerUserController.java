package com.akk.controller;

import com.akk.config.ProjectUrlConfig;
import com.akk.constant.CookieConstant;
import com.akk.constant.RedisConstant;
import com.akk.dataobject.SellerInfo;
import com.akk.enums.ResultEnum;
import com.akk.service.SellerService;
import com.akk.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户登录管理
 * Created by Akk_Mac
 * Date: 2017/8/30 上午9:31
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    //redis的service,这里主要用stringredis
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "username", required=false ) String username,
                              @RequestParam(value = "password", required = false) String password,
                              HttpServletResponse response,
                      Map<String, Object> map) {
        //1. 由于我们这里没有申请微信开放平台，所以就不用扫码登录了
        if(username == null && password == null){
            return new ModelAndView("common/login");
        }

        SellerInfo sellerInfo = sellerService.findSellerInfoByUsername(username);
        if(sellerInfo == null && !sellerInfo.getPassword().equals(password)) {
            map.put("msg", ResultEnum.LGOIN_FAIL.getMessage());
            //map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/login", map);
        }
        //2. 设置token至redis(用什么UUID设置)
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;//token过期时间
        //(key:token_ 为开头的格式String.format是格式设置方法, value=这里先设置为username, 过期时间, 时间单位)
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), username, expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        //这里不是跳转到模板而是地址所以要用redirect,而且跳转最好用绝对地址
        return new ModelAndView("redirect:" + projectUrlConfig.getSell()  + "/sell/seller/order/list");

    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> map) {
        //1. 从cookie中查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie != null) {
            //2. 清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

            //3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }
        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/sell/seller/login");
        return new ModelAndView("common/success", map);
    }
}
