package com.akk.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层对象，面对前端一般需要序列化
 * Created by KHM
 * 2017/7/26 17:43
 */
@Data
public class ResultVO<T> implements Serializable {

    //序列化
    private static final long serialVersionUID = -8428042511623731358L;

    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //具体内容
    private T data;

}
