package com.akk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目中的url统一配置
 * Created by Akk_Mac
 * Date: 2017/8/30 上午9:23
 */
@Data
@ConfigurationProperties(prefix = "projectUrl")
@Component
public class ProjectUrlConfig {

    /**
     * 微信公众账号授权url
     */
    public String wechatMpAuthorize;

    //公共平台就不写了，要钱

    /**
     * 点餐系统
     */
    public String sell;

}
