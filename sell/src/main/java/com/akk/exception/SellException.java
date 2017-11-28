package com.akk.exception;

import com.akk.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by KHM
 * 2017/7/27 17:34
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        //把message传到父类的构造方法里,相当于覆盖message
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
