package com.step.fastpda;

import android.app.Application;

import com.tech.libnetwork.ApiService;

/**
 * Created by user on 2019-07-30.
 */

public class PdaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("https://www.fastmock.site/mock/16848d2a2b13c1bc0176b0e2b95ad6f6/api", null);
//        Pda.init(this)
//                .withApiHost("http://192.168.20.44:5000/")
//                .withInterceptor(MainInterceptor.get())
//                .withInterceptor(new DebugInterceptor("mini/page",R.raw.mini_pack))
//                .withInterceptor(new DebugInterceptor("LoginUser",R.raw.login_user))
//                .withInterceptor(new DebugInterceptor("bigpack",R.raw.big_pack))
//                .withInterceptor(new DebugInterceptor("Data/ParseBarcode",R.raw.parse_bar_code))
//                .withIcon(new FontAwesomeModule())
//                .configure();
//
//        DatabaseManager.getInstance().init(this);
    }
}
