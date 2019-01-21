package com.example.lenovo.httpapi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public static final String PREFERENCE_PACKAGE ="com.example.lenovo.httpapi";
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";

    EditText username;
    Button email_sign_in_button;
    private String clientId     = "05ca665f7029a693f582";
    private String clientSecret = "327818d5ed1205f145de819901ed016bcc1915d0";
    private String redirectUri  = "wangjiwu://callback";
    String inputName = "";
    private String baseURL = "https://api.github.com/";
    //使用跟安全的oauth 登陆方式
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        email_sign_in_button = findViewById(R.id.email_sign_in_button);

       //如果没有用户登陆过
        SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

        String loginNamebefore = sharedPreferences.getString("loginIn", "none");
        String token = sharedPreferences.getString(loginNamebefore, "none");

        if (loginNamebefore.equals("none")) {

        } else {
            username.setText(loginNamebefore);
            inputName = username.getText().toString();
            if (inputName.equals("")) {
                Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            } else if (!token.equals("none")){
                Toast.makeText(LoginActivity.this, "自动登陆", Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(LoginActivity.this,ForeActivity.class);
                intent.putExtra("access_token",token);
                intent.putExtra("username",inputName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }


        email_sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inputName = username.getText().toString();
                String token = "";
                if (inputName.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

                    token = sharedPreferences.getString(inputName, "none");
                     SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loginIn", inputName);
                    editor.commit();
                    if (token.equals("none")) {
                        onClickGetAccessToken();
                    } else {


                        Intent intent =new Intent(LoginActivity.this,ForeActivity.class);
                        intent.putExtra("access_token",token);
                        intent.putExtra("username",inputName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if ( uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            GitHubService client = retrofit.create(GitHubService.class);

            Call<AccessToken> accessTokenCall =
                    client.getAccessToken(clientId, clientSecret, code);

            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    AccessToken accessToken = response.body();
                    getLoginInfo(accessToken.getAccessToken());

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "No!", Toast.LENGTH_SHORT).show();
                }
            });



            Toast.makeText(LoginActivity.this, "登陆成功，正在跳转", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    public void onClickGetAccessToken() {
        //通过传入login 参数实现指定用户登陆和验证
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "https://github.com/login/oauth/authorize" + "?client_id=" + clientId
                        + "&scope=repo,user" + "&redirect_uri=" + redirectUri + "&login=" + inputName
        ));

        startActivity(intent);
    }

    private void getLoginInfo(final String key_word) {

        //设置头的token
        OkHttpClient build = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)

                // 本次实验不需要自定义Gson
                .addConverterFactory(GsonConverterFactory.create())
                // build 即为okhttp声明的变量，下文会讲
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();


        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装


        Call<Follower> list_Event = request.getLoginInfo(key_word);

        list_Event.enqueue(new Callback<Follower>() {
            @Override
            public void onResponse(Call<Follower> call, Response<Follower> response) {
                try {
                    Follower returns = response.body();
                    inputName = returns.getLogin();
                    Intent intent =new Intent(LoginActivity.this,ForeActivity.class);
                    intent.putExtra("access_token",key_word);
                    intent.putExtra("username",inputName);

                    //保存 用户名和token 防止重复登陆
                    SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(inputName, key_word);
                    editor.putString("loginIn", inputName);
                    editor.commit();

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<Follower> call, Throwable t) {

            }
        });
    }





}

