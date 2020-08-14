package com.step.fastpda.ui.shipping;

import java.io.Serializable;

/**
 * @author zhushubin
 * @email 604580436@qq.com
 * @date 2020/8/14 0014 下午 9:38
 */
public class ResponseInfo implements Serializable {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
