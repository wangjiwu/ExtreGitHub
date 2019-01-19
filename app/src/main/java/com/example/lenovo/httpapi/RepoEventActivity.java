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
import java.util.List;
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

public class RepoEventActivity extends AppCompatActivity {

    String repoName, userName,flag = "", LoginName;
    private EventAdapter myAdapteEvent;
    private GistAdapter myAdapteGist;
    ProgressBar loddingBar;
    RecyclerView recyclerView2;
    ImageView go_back,reflash;
    TextView lable, nodata;
    Retrofit retrofit;
    private RecyclerView.LayoutManager mLayoutManager;
    ////////////////////////////
    private String baseURL = "https://api.github.com/";
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";
    private String access_token = "";
    ////////////////////////////


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getString("flag").equals("gist")){
                ArrayList<Gist> list =  (ArrayList<Gist>)msg.getData().getSerializable("msg");

                if (list != null) {

                    if (list.size() == 0) {
                        nodata.setVisibility(View.VISIBLE);

                    }

                    try {
                        myAdapteGist.addObjects(list);
                    } catch (Exception e) {
                        Toast.makeText(RepoEventActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }

            } else {
                ArrayList<Event> EventList = (ArrayList<Event>) msg.getData().getSerializable("msg");
                if (EventList == null) {

                    nodata.setVisibility(View.VISIBLE);

                } else {

                    if (EventList.size() == 0) {
                        nodata.setVisibility(View.VISIBLE);

                    }

                    myAdapteEvent.addObjects(EventList);
                }

            }
            loddingBar.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_event);

        try {
            repoName =  getIntent().getStringExtra("repoName");
            userName =  getIntent().getStringExtra("userName");
            flag =  getIntent().getStringExtra("flag");
            LoginName = getIntent().getStringExtra("LoginName");
        } catch (Exception e) {

        }

        SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

        String loginNamebefore = sharedPreferences.getString("loginIn", "none");


        access_token = sharedPreferences.getString(loginNamebefore, "");


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


        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)

                // 本次实验不需要自定义Gson
                .addConverterFactory(GsonConverterFactory.create())
                // build 即为okhttp声明的变量，下文会讲
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();


        nodata =  findViewById(R.id.nodata);
        lable = findViewById(R.id.lable);
        reflash = findViewById(R.id.reflash);
        loddingBar = findViewById(R.id.loddingBar);
        recyclerView2 = findViewById(R.id.recyclerView2);
        myAdapteEvent = EventAdapter.getInstance();
        myAdapteEvent.initContext(this);
        myAdapteGist = GistAdapter.getInstance();
        myAdapteGist.initContext(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(mLayoutManager);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteEvent);
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

        reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loddingBar.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.INVISIBLE);
                if (flag.equals("showGist")) {
                    getGistInfo();
                } else {
                    getEventInfo();
                }
            }
        });
        nodata.setVisibility(View.INVISIBLE);
        loddingBar.setVisibility(View.VISIBLE);
        if (flag.equals("showGist")) {
            lable.setText("Gist");
            getGistInfo();
        } else {
            lable.setText("Event");
            getEventInfo();
        }




    }


    void getEventInfo() {
        myAdapteEvent.cleanList();
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteEvent);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));



        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装

        Call<ArrayList<Event>> list = null;

        if (flag.equals("fromRepo")){
            list =  request.getRepoEvent(userName,repoName);
        } else if (flag.equals("fromOwner")) {
            list =  request.getEvents(LoginName);
        }


        list.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                try {
                    ArrayList<Event> repoEvent = response.body();
                    Bundle b = new Bundle();
                    b.putString("flag", "event");
                    b.putSerializable("msg",(Serializable) repoEvent);
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(RepoEventActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

            }
        });
    }

    void  getGistInfo(){
        myAdapteGist.cleanList();
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteGist);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        recyclerView2.setAdapter((scaleInAnimationAdapter));



        GitHubService request = retrofit.create(GitHubService.class);

        Call<ArrayList<Gist>> list_Event = request.getGists(LoginName);

        list_Event.enqueue(new Callback<ArrayList<Gist>>() {
            @Override
            public void onResponse(Call<ArrayList<Gist>> call, Response<ArrayList<Gist>> response) {
                try {
                    ArrayList<Gist> list = response.body();

                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list);
                    b.putString("flag", "gist");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(RepoEventActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Gist>> call, Throwable t) {

            }
        });


    }

}
