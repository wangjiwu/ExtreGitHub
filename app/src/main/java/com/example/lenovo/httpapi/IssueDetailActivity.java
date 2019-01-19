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
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueDetailActivity extends AppCompatActivity {

    private TextView issue_body,issue_pusher, issue_update_time, issue_num;
    private ImageView pusher_icon;
    private ImageView  go_back,share,faver;
    private RecyclerView issueRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentAdapter myAdapterComment;
    Issue detailIssue = null;
    private String baseURL = "https://api.github.com/";

    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<Comment> list =  (ArrayList<Comment>)msg.getData().getSerializable("msg");


            if (list == null) {
                Toast.makeText(IssueDetailActivity.this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    myAdapterComment.addObjects(list);

                } catch (Exception e) {
                    Toast.makeText(IssueDetailActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_detail);

        issue_body = findViewById(R.id.issue_body);
        issue_pusher = findViewById(R.id.issue_pusher);
        issue_update_time = findViewById(R.id.issue_update_time);
        issue_num = findViewById(R.id.issue_num);
        pusher_icon = findViewById(R.id.pusher_icon);
        go_back = findViewById(R.id.go_back);
        share = findViewById(R.id.share);
        faver = findViewById(R.id.faver);
        issueRecycleView =  findViewById(R.id.issueRecycleView);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myAdapterComment =  CommentAdapter.getInstance().getInstance();
        myAdapterComment.initContext(this);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterComment);
        scaleInAnimationAdapter.setDuration(500);
        issueRecycleView.setAdapter((scaleInAnimationAdapter));
        issueRecycleView.setLayoutManager(mLayoutManager);
        detailIssue = (Issue) getIntent().getExtras().getSerializable("issue");

        issue_body.setText(detailIssue.getBody());
        issue_pusher.setText(detailIssue.getUser().getLogin());
        issue_update_time.setText(detailIssue.getCreated_at());
        issue_num.setText(detailIssue.getNumber());

        Picasso.with(IssueDetailActivity.this).load(detailIssue.getUser().getAvatar_url())
                .noFade()
                .into( pusher_icon);


        getIssueComment();


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getIssueComment() {
        SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );

        String loginNamebefore = sharedPreferences.getString("loginIn", "none");


        final String access_token = sharedPreferences.getString(loginNamebefore, "");


        baseURL = detailIssue.getUrl() + "/";
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
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterComment);
        scaleInAnimationAdapter.setDuration(500);
        issueRecycleView.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装




        Call<ArrayList<Comment>> list_repo = request.getIssueComments();

        list_repo.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                try {
                    ArrayList<Comment> list = response.body();

                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list);

                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);


                } catch (Exception e) {
                    Toast.makeText(IssueDetailActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });

    }


}
