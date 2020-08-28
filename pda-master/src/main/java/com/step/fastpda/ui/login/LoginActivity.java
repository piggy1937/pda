package com.step.fastpda.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.TypeReference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.step.fastpda.MainActivity;
import com.step.fastpda.R;
import com.step.fastpda.model.User;
import com.step.fastpda.utils.PreferenceUtils;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;

import java.util.Date;

import static com.tech.libnetwork.Request.NET_ONLY;

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
    private boolean isHidden=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_login);
        mUsername= findViewById(R.id.edit_sign_in_username);
        mPassword= findViewById(R.id.edit_sign_in_password);

        IconDrawable iconDrawable = new IconDrawable(this, FontAwesomeIcons.fa_eye)
                .colorRes(R.color.color_666)
                .actionBarSize();
        mPassword.setCompoundDrawables(null, null, iconDrawable, null);
        mPassword.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getCompoundDrawables() 可以获取一个长度为4的数组，
                // 存放drawableLeft，Right，Top，Bottom四个图片资源对象
                // index=2 表示的是 drawableRight 图片资源对象
                Drawable drawable = mPassword.getCompoundDrawables()[2];
                //如果右边没有图片，不做处理
                if (drawable == null) {
                    return false;
                }
                //如果不是按下事件，不做处理
                if(event.getAction()!=MotionEvent.ACTION_UP){
                    return false;
                }
                //drawable.getIntrinsicWidth() 获取drawable资源图片呈现的宽度
                if(event.getX()>mPassword.getWidth()- mPassword.getPaddingRight()-drawable.getIntrinsicWidth()){

                    if (isHidden) {
                        //设置EditText文本为可见的
                        mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //设置EditText文本为隐藏的
                        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    isHidden=!isHidden;

                }
                return false;
            }
        });


        mApiHost= findViewById(R.id.edit_sign_in_host);
        actionLogin= findViewById(R.id.action_login);
        actionLogin.setOnClickListener(this);

        String sUsername= PreferenceUtils.getString(LoginActivity.this,"USER_NAME","");
        String sPassword= PreferenceUtils.getString(LoginActivity.this,"PASSWORD","");
        String sApiHost= PreferenceUtils.getString(LoginActivity.this,"API_HOST","http://114.88.75.45:1234");
        mUsername.setText(sUsername);
        mPassword.setText(sPassword);
        mApiHost.setText(sApiHost);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!UserManager.get().isLogin()) {
            String sPassword = PreferenceUtils.getString(LoginActivity.this, "PASSWORD", "");
            mPassword.setText(sPassword);
        }
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
        new AsyncTask<String, Void, BaseResponseInfo>() {
            //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
            @Override
            protected BaseResponseInfo doInBackground(String... params) {
                ApiResponse apiResponse= ApiService.post("/Data/Login")
                        .cacheStrategy(NET_ONLY)
                        .addParam("username",params[0])
                        .addParam("password",params[1])
                        .responseType(new TypeReference<BaseResponseInfo>(){}.getType())
                        .execute();
                if(apiResponse!=null&&apiResponse.body!=null){
                    return (BaseResponseInfo) apiResponse.body;
                }
                return null;
            }

            //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
            // 后台的计算结果将通过该方法传递到UI线程，并且在界面上展示给用户.
            @Override
            protected void onPostExecute(BaseResponseInfo responseInfo) {
                if(responseInfo==null){
                    Toast.makeText(LoginActivity.this,"用户名密码错误",Toast.LENGTH_LONG).show();
                    return;
                }
                if(responseInfo.getErrCode().equals("0")){
                    Toast.makeText(LoginActivity.this,"用户名密码错误",Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User();

                user.setExpires_time(new Date().getTime()+2*60*60*1000);
                user.setName(userName);
                user.setAvatar(R.mipmap.avatar+"");
                user.setDescription("这家伙很懒,啥也没有写");
                UserManager.get().save(user);
                String apiHost=mApiHost.getText().toString();
                if(apiHost!=null&&!apiHost.isEmpty()){
                    ApiService.init(apiHost.toString(),null);
                }
                PreferenceUtils.putString(LoginActivity.this,"USER_NAME", userName);
                PreferenceUtils.putString(LoginActivity.this,"PASSWORD", password);
                PreferenceUtils.putString(LoginActivity.this,"API_HOST", apiHost);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }.execute(userName,password);

    }
}
