package com.example.lenovo.httpapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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


public class OwnerDetailActivity extends Activity {

    XCRoundImageView OwnerIcon;
    TextView ownerName, Jointime,email_text, Blog_text,location_text, company_text ;
    private String baseURL = "https://api.github.com/";
    private String access_token = "";
    String LoginName;

    ProgressBar loddingBar2;
    ImageView go_back, share;
    LinearLayout poster;
    Button events,organize,starred,followers,following,gists, repository;
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            loddingBar2.setVisibility(View.INVISIBLE);
            Owner owner = (Owner) msg.getData().getSerializable("msg");
            if (owner.getEmail() != null) {
                email_text.setText(owner.getEmail());
            }


            Picasso.with(OwnerDetailActivity.this).load(owner.getAvatar_url())
                    .noFade()
                    .into( OwnerIcon);
            if (owner.getLogin() != null) {
                ownerName.setText(owner.getLogin());
            }

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    //加载成功后会得到一个bitmap,可以自定义操作
                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                    drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
                    poster.setBackground(drawable);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    // 加载失败进行相应处理
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(OwnerDetailActivity.this).load(owner.getAvatar_url())
                    .noFade()
                    .resize(400,300)
                    .into(target);


            Jointime.setText(owner.getCreated_at().substring(0,10));

            if (owner.getBlog() != null  ) {
                if (owner.getBlog().equals("")){
                    Blog_text.setText("Not set");
                } else {
                    Blog_text.setText(owner.getBlog());
                }
            }

            if (owner.getLocation() != null) {
                if (owner.getLocation().equals("")){
                    location_text.setText("Not set");
                } else {
                    location_text.setText(owner.getLocation());
                }
            }

            if (owner.getCompany() != null) {
                company_text.setText(owner.getCompany());
            }
            loddingBar2.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_detail);
        OwnerIcon = findViewById(R.id.OwnerIcon);
        ownerName = findViewById(R.id.ownerName);
        Jointime = findViewById(R.id.Jointime);
        email_text = findViewById(R.id.email_text);
        Blog_text = findViewById(R.id.Blog_text);
        location_text = findViewById(R.id.location_text);
        company_text = findViewById(R.id.company_text);
        go_back = findViewById(R.id.go_back);
        loddingBar2 = findViewById(R.id.loddingBar2);

        events = findViewById(R.id.events);
        organize = findViewById(R.id.organize);
        starred = findViewById(R.id.starred);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        gists = findViewById(R.id.gists);
        repository = findViewById(R.id.repository);
        poster = findViewById(R.id.poster);
         go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try{
            LoginName = getIntent().getStringExtra("ownerName");

            SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

            String loginNamebefore = sharedPreferences.getString("loginIn", "none");


            access_token = sharedPreferences.getString(loginNamebefore, "");

            getOwnerInfo();
        } catch (Exception e) {

        }

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoEventActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","fromOwner");
                startActivity(intent);
            }
        });

        starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoForksActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","fromOwnerStar");
                startActivity(intent);
            }
        });

        repository.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoForksActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","fromOwnerRepo");
                startActivity(intent);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoStargazerActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","fromOwnerFollowers");
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoStargazerActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","fromOwnerFollowing");
                startActivity(intent);
            }
        });
        gists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(OwnerDetailActivity.this,RepoEventActivity.class);
                intent.putExtra("LoginName",LoginName );
                intent.putExtra("flag","showGist");
                startActivity(intent);
            }
        });







    }

    private void getOwnerInfo() {

        OkHttpClient build = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("User-Agent", "ExtreGithub")
                                .header("Authorization", "token " + access_token);
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
        Call<Owner> list_Event = request.getBaseUserInfo(LoginName);

        list_Event.enqueue(new Callback<Owner>() {
            @Override
            public void onResponse(Call<Owner> call, Response<Owner> response) {
                try {
                    Owner owner = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) owner);
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(OwnerDetailActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<Owner> call, Throwable t) {

            }
        });
    }
}
