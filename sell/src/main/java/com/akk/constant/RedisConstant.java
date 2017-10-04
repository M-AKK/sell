package com.akk.constant;

/**
 * redis常亮
 * Created by Akk_Mac
 * Date: 2017/8/30 上午10:09
 */
public interface RedisConstant {

    //给每个token一个前缀
    String TOKEN_PREFIX = "token_%s";

    Integer EXPIRE = 7200; //2h
}
