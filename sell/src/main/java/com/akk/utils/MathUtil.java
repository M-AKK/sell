package com.akk.utils;

/**
 * 比较Double和BigDecimal类型数值的方法
 * Created by Akk_Mac
 * Date: 2017/8/26 下午12:41
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较2个金额是否相等
     * @param d1
     * @param d2
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        if(result < MONEY_RANGE){
            return true;
        } else {
            return false;
        }
    }
}
