package com.tech.libcommon.global;

import androidx.annotation.Nullable;

/**
 * @author zhushubin
 * @email 604580436@qq.com
 * @date 2020/8/15 0015 上午 7:38
 */
public interface IGlobalCallback<T> {
    void executeCallback(@Nullable T args);
}
