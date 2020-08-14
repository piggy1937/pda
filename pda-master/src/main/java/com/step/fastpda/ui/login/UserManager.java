package com.step.fastpda.ui.login;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.step.fastpda.model.User;
import com.tech.libcommon.global.AppGlobals;
import com.tech.libnetwork.cache.CacheManager;

/**
 * @author zhushubin
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class UserManager {
    private static final String KEY_CACHE_USER = "cache_user";
    private static UserManager mUserManager = new UserManager();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private User mUser;

    public static UserManager get() {
        return mUserManager;
    }

    private UserManager() {
        User cache = (User) CacheManager.getCache(KEY_CACHE_USER);
        if (cache != null && cache.expires_time > System.currentTimeMillis()) {
            mUser = cache;
        }
    }

    public void save(User user) {
        mUser = user;
        CacheManager.save(KEY_CACHE_USER, user);
        if (userLiveData.hasObservers()) {
            userLiveData.postValue(user);
        }
    }

    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return userLiveData;
    }

    public boolean isLogin() {
        return mUser == null ? false : mUser.expires_time > System.currentTimeMillis();
    }

    public User getUser() {
        return isLogin() ? mUser : null;
    }

    public long getUserId() {
        return isLogin() ? mUser.userId : 0;
    }


    public LiveData<User> refresh() {
        if (!isLogin()) {
            return login(AppGlobals.getApplication());
        }
        MutableLiveData<User> liveData = new MutableLiveData<>();

        return liveData;
    }

    public void logout() {
        CacheManager.delete(KEY_CACHE_USER, mUser);
        mUser = null;
    }
}
