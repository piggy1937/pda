package com.step.fastpda.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.TypeReference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.collect.Lists;
import com.step.fastpda.MainActivity;
import com.step.fastpda.R;
import com.step.fastpda.model.User;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;

import java.util.Date;
import java.util.List;

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
        String userName=mUsername.getText().toString();
        String password=mPassword.getText().toString();
        new AsyncTask<String, Void, LoginInfo>() {
            //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
            @Override
            protected LoginInfo doInBackground(String... params) {
                ApiResponse apiResponse= ApiService.post("/login")
                        .addParam("username",params[0])
                        .addParam("password",params[1])
                        .responseType(new TypeReference<LoginInfo>(){}.getType())
                        .execute();
                if(apiResponse!=null&&apiResponse.body!=null){
                    return (LoginInfo) apiResponse.body;
                }
                return null;
            }

            //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
            // 后台的计算结果将通过该方法传递到UI线程，并且在界面上展示给用户.
            @Override
            protected void onPostExecute(LoginInfo loginInfo) {
                if(loginInfo==null){
                    Toast.makeText(LoginActivity.this,"用户名密码错误",Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User();
                Log.e("user",loginInfo.getUsername());
                user.setExpires_time(new Date().getTime()+2*60*60*1000);
                user.setName(loginInfo.getUsername());
                UserManager.get().save(user);
                String apiHost=mApiHost.getText().toString();
                if(apiHost!=null&&!apiHost.isEmpty()){
                    ApiService.init(apiHost.toString(),null);
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }.execute(userName,password);

    }
}
