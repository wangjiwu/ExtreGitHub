package com.example.lenovo.httpapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoContributorsActivity extends AppCompatActivity {

    String repoName, userName;
    private FollowAdapter myAdapteStargazer;
    ProgressBar loddingBar;
    RecyclerView recyclerView2;
    ImageView go_back;
    private RecyclerView.LayoutManager mLayoutManager;
    ////////////////////////////
    private String baseURL = "https://api.github.com/";
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";



    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            ArrayList<Follower> EventList = (ArrayList<Follower>) msg.getData().getSerializable("msg");
            if (EventList == null) {
                Toast.makeText(RepoContributorsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            } else {
                myAdapteStargazer.addObjects(EventList);
            }
            loddingBar.setVisibility(View.INVISIBLE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_stargazer);
        repoName =  getIntent().getStringExtra("repoName");
        userName =  getIntent().getStringExtra("userName");
        loddingBar = findViewById(R.id.loddingBar);
        recyclerView2 = findViewById(R.id.recyclerView2);
        myAdapteStargazer = FollowAdapter.getInstance();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(mLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteStargazer);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        recyclerView2.setItemAnimator(new OvershootInLeftAnimator());
        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );
        loddingBar.setVisibility(View.VISIBLE);
        String loginNamebefore = sharedPreferences.getString("loginIn", "none");

        final String access_token = sharedPreferences.getString(loginNamebefore, "");
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
        ////////////////////////////




        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装
        Call<ArrayList<Follower>> list_Event = request.getRepoStargazers(userName,repoName);

        list_Event.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                try {
                    ArrayList<Follower> repoStar_follow = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) repoStar_follow);
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(RepoContributorsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {

            }
        });


    }
}
