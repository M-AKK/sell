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
        //把枚举中自己定义的message传到父类的构造方法里,相当于覆盖message
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    //而这个是需要自己去填写code的新的meg，不一定是枚举中的模糊的说法，可以把具体的错误信息信使出来
    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
