package com.akk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 把对象转换为json格式，方便查看
 * Created by Akk_Mac
 * Date: 2017/8/25 下午6:07
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
