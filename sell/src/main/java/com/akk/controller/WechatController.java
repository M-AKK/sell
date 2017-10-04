package com.akk.controller;

import com.akk.config.ProjectUrlConfig;
import com.akk.enums.ResultEnum;
import com.akk.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * 这里使用第三方SDK的方式获取openid，原始方法参考eclipse的项目
 * Created by Akk_Mac
 * Date: 2017/8/24 下午4:51
 */
//@RestController//是返回json的，不会跳转，所以这里用controller
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //这也是点击项目首页后第一个调用的方法，获取openid
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1.配置
        //2.调用方法
        String url = projectUrlConfig.getWechatMpAuthorize() + "/sell/wechat/userInfo";
        //这里是根据配置的方法去重定向到下面一个方法得到返回值，这里主要是要为了获取code
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
        //log.info("【微信网页授权】获取code，result={}", redirectUrl);
        //重定向必须要加"redirect:"拼接，否则要像ssm一样配置好
        return "redirect:" + redirectUrl;
    }

    //这里得到code和目标地址，如果不是用微信客户顿打开的话就会重定向到另一个地址，叫你用客户端打开
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        //我们是为了网页支付，而网页支付主要是要openid
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl +"?openid=" + openId;
    }
}








