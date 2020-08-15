package com.tech.libcommon.global;

import java.util.WeakHashMap;

/**
 * @author zhushubin
 * @email 604580436@qq.com
 * @date 2020/8/15 0015 上午 7:37
 * 回调管理
 */
public class CallbackManager {
    private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static class Holder {
        private static final CallbackManager INSTANCE = new CallbackManager();
    }

    public static CallbackManager getInstance() {
        return Holder.INSTANCE;
    }

    public CallbackManager addCallback(Object tag, IGlobalCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }

    public IGlobalCallback getCallback(Object tag) {
        return CALLBACKS.get(tag);
    }
}
