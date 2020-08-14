package com.step.fastpda.ui.login;

import java.io.Serializable;

/**
 * @author zhushubin
 * @email 604580436@qq.com
 * @date 2020/8/14 0014 下午 8:13
 */
public class LoginInfo implements Serializable {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
