package com.step.fastpda.ui.login;

import java.io.Serializable;

/**
 * @author zhushubin
 * @date 2020-08-27.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class BaseResponseInfo implements Serializable {
    private String  errCode;
    private String  errMsg;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
