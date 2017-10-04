package com.akk.utils;

import com.akk.enums.CodeEnum;

/**
 * 还没看懂
 * Created by Akk_Mac
 * Date: 2017/8/27 上午10:46
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for(T each : enumClass.getEnumConstants()) {
            if(code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

}
