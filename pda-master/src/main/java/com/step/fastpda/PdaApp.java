package com.step.fastpda;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.tech.libnetwork.ApiService;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by user on 2019-07-30.
 */

public class PdaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://192.168.20.44:5000", null);
        Iconify.with(new FontAwesomeModule());
        CrashReport.initCrashReport(getApplicationContext(), "8af2af4e98", true);
    }
}
