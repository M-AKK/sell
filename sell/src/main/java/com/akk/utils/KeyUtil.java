package com.akk.utils;

import java.util.Random;

/**
 * Created by KHM
 * 2017/7/27 17:50
 */
public class KeyUtil {

    /**
     * 生成唯一的主键(订单Id)
     * 格式：时间+随机数
     * 防止多线程会重复要加synchronized
     */
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        System.currentTimeMillis();
        //这样生成的随机数就是6位了
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
