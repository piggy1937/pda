package com.step.fastpda.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.step.fastpda.R;
import com.step.fastpda.model.User;

/**
 * @author zhushubin
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private TextInputEditText mApiHost;
    private MaterialButton actionLogin;
    private View actionClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_login);
        mUsername= findViewById(R.id.edit_sign_in_username);
        mPassword= findViewById(R.id.edit_sign_in_password);
        mApiHost= findViewById(R.id.edit_sign_in_host);
        actionLogin= findViewById(R.id.action_login);
        actionLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_login) {
            login();
        }
    }

    /***
     * 登录系统
     */
    private void login() {
        User user = new User();
        user.setName("张三");
        UserManager.get().save(user);
        finish();
    }
}
