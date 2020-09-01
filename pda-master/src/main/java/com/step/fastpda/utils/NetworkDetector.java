package com.step.fastpda.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author zhushubin
 * @email 604580436@qq.com
 * @date 2020/8/30 0030 下午 4:47
 * 工具类-网络是否可用
 */
public class NetworkDetector {
    public static boolean detect(Context context) {

        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }
}
