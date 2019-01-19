package com.example.lenovo.httpapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class RepoForksActivity extends AppCompatActivity {

    String repoName, userName, flag = "", LoginName;
    private RepoAdapter myAdapteForks;
    ProgressBar loddingBar;
    RecyclerView recyclerView2;
    ImageView go_back,reflash;
    TextView lable,nodata;

    private RecyclerView.LayoutManager mLayoutManager;
    private String baseURL = "https://api.github.com/";
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";



    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            ArrayList<Repo> repo_fork_list = (ArrayList<Repo>) msg.getData().getSerializable("msg");


            if (repo_fork_list == null) {
                nodata.setVisibility(View.VISIBLE);
            } else {
                if(repo_fork_list.size() == 0) {
                    nodata.setVisibility(View.VISIBLE);
                }

                myAdapteForks.addObjects(repo_fork_list);
            }
            loddingBar.setVisibility(View.INVISIBLE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_forks);

        flag = getIntent().getStringExtra("flag");

        loddingBar = findViewById(R.id.loddingBar);
        recyclerView2 = findViewById(R.id.recyclerView2);
        myAdapteForks = RepoAdapter.getInstance();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(mLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteForks);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        recyclerView2.setItemAnimator(new OvershootInLeftAnimator());
        go_back = findViewById(R.id.go_back);
        lable =  findViewById(R.id.lable);
        reflash = findViewById(R.id.reflash);
        nodata = findViewById(R.id.nodata);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForkInfo();

            }
        });
        getForkInfo();


    }

    private  void getForkInfo() {

        myAdapteForks.cleanList();
        loddingBar.setVisibility(View.VISIBLE);
        nodata.setVisibility(View.INVISIBLE);


        SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

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


        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装
        Call<ArrayList<Repo>> list = null;

        if (flag.equals("fromOwnerStar")) {
            lable.setText("Starred");
            LoginName = getIntent().getStringExtra("LoginName");
            list = request.getStarred(LoginName);
        } else  if (flag.equals("fromOwnerRepo")) {
            lable.setText("Repository");
            LoginName = getIntent().getStringExtra("LoginName");
            list = request.getRepo(LoginName);
        } else if (flag.equals("fromRepoDetail")) {
            lable.setText("Fork");
            repoName =  getIntent().getStringExtra("repoName");
            userName =  getIntent().getStringExtra("userName");
            list = request.getRepoForks(userName,repoName);
        }



        list.enqueue(new Callback<ArrayList<Repo>>() {
            @Override
            public void onResponse(Call<ArrayList<Repo>> call, Response<ArrayList<Repo>> response) {
                try {
                    ArrayList<Repo> repo_fork_list = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) repo_fork_list);
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(RepoForksActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repo>> call, Throwable t) {

            }
        });

    }

}
