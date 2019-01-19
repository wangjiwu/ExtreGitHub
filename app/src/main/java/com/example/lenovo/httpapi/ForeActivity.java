package com.example.lenovo.httpapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

public class ForeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SystemBarTintManager tintManager;
    private NavigationView navigationView;
    private String username = "";
    private String access_token = "";
    ImageView menu, reflash;
    TextView viewName, userName, joinAt,nodata;
    RadioGroup switchGroup, switchGroup_search;
    ImageView personImage;
    SearchView search_text;
    ConstraintLayout poster;
    public RecyclerView recyclerView2;
    String search_key_word = "";
    private RecyclerView.LayoutManager mLayoutManager;
    private RepoAdapter myAdapterRepo;
    private IssueAdapter myAdapterIssue;
    private FollowAdapter myAdapterFollow;
    private EventAdapter myAdapteEvent;
    private GistAdapter myAdapteGist;
    private String baseURL = "https://api.github.com/";
    boolean SearchRepo = true;
    private ProgressBar  loddingBar;
    private android.support.design.widget.FloatingActionButton  floatBtn;
    Retrofit retrofit;
    ArrayList<Repo> list_lase_issue;




    @SuppressLint("HandlerLeak")
            private final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

            if (msg.getData().getString("flag").equals("repo")) {
                ArrayList<Repo> list =  (ArrayList<Repo>)msg.getData().getSerializable("msg");
                if (list.size()==0) {
                    nodata.setVisibility(View.VISIBLE);
                }

                if (list == null) {
                    Toast.makeText(ForeActivity.this, "此用户没有任何仓库", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        myAdapterRepo.addObjects(list);

                    } catch (Exception e) {
                        Toast.makeText(ForeActivity.this, "数据库中不存在记录", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (msg.getData().getString("flag").equals("issue")){
                ArrayList<Issue> list =  (ArrayList<Issue>)msg.getData().getSerializable("msg");
                nodata.setVisibility(View.INVISIBLE);
                Log.d("fuckbug",  String.valueOf(list.size()));

                if (list == null) {
                    Toast.makeText(ForeActivity.this, "此用户没有任何issue", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        for (int i = 0; i < list.size(); i++) {
                            myAdapterIssue.addObject(list.get(i));
                        }

                    } catch (Exception e) {
                        Toast.makeText(ForeActivity.this, "数据库中不存在issue", Toast.LENGTH_SHORT).show();
                    }
                }


            }else if (msg.getData().getString("flag").equals("follow")){
                ArrayList<Follower> list =  (ArrayList<Follower>)msg.getData().getSerializable("msg");
                if (list.size()==0) {
                    nodata.setVisibility(View.VISIBLE);
                }


                if (list == null) {

                } else {
                    try {

                        myAdapterFollow.addObjects(list);

                    } catch (Exception e) {
                        Toast.makeText(ForeActivity.this, "数据库中不存在issue", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if (msg.getData().getString("flag").equals("event")){
                ArrayList<Event> list =  (ArrayList<Event>)msg.getData().getSerializable("msg");

                if (list != null) {
                    try {
                        myAdapteEvent.addObjects(list);
                    } catch (Exception e) {
                        Toast.makeText(ForeActivity.this, "数据库中不存在event", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if (msg.getData().getString("flag").equals("gist")){
                ArrayList<Gist> list =  (ArrayList<Gist>)msg.getData().getSerializable("msg");

                if (list.size()==0) {
                    nodata.setVisibility(View.VISIBLE);
                }

                if (list != null) {
                    try {
                        myAdapteGist.addObjects(list);
                    } catch (Exception e) {


                    }
                } else {

                }
            }else if (msg.getData().getString("flag").equals("owner")) {

                Owner owner = (Owner) msg.getData().getSerializable("msg");

                try {
                    Picasso.with(ForeActivity.this).load(owner.getAvatar_url())
                            .noFade()
                            .into( personImage);

                    userName.setText(owner.getLogin());
                    joinAt.setText(owner.getCreated_at().substring(0, 10));
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

                    Picasso.with(ForeActivity.this).load(owner.getAvatar_url())
                            .noFade()
                            .resize(400,200)
                            .into(target);

                } catch (Exception e) {

                }



            }
            loddingBar.setVisibility(View.INVISIBLE);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore);
        access_token = getIntent().getStringExtra("access_token");
        username = getIntent().getStringExtra("username");

        //设置头的token
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




        initWindow();
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
        navigationView = (NavigationView) findViewById(R.id.nav);
        menu= (ImageView) findViewById(R.id.main_menu);
        viewName = findViewById(R.id.viewName);
        nodata= findViewById(R.id.nodata);
        switchGroup = findViewById(R.id.switchGroup);
        reflash= findViewById(R.id.reflash);
        floatBtn =  findViewById(R.id.floatBtn);
        switchGroup_search  = findViewById(R.id.switchGroup_search);
        search_text = findViewById(R.id.search_text);
        View headerView = navigationView.getHeaderView(0);//获取头布局
        personImage = headerView.findViewById(R.id.personImage);
        poster =   headerView.findViewById(R.id.poster);

        userName = headerView. findViewById(R.id.userName);
        joinAt =  headerView.findViewById(R.id.joinAt);

        getOwnerInfo();
        loddingBar = findViewById(R.id.loddingBar);

        switchGroup_search.setVisibility(View.VISIBLE);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ForeActivity.this,OwnerDetailActivity.class);
                intent.putExtra("ownerName", username);
                intent.putExtra("access_token", access_token);
                startActivity(intent);
            }
        });

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView2.smoothScrollToPosition(0);
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.main_menu://点击菜单，跳出侧滑菜单
                        if (drawerLayout.isDrawerOpen(navigationView)){
                            drawerLayout.closeDrawer(navigationView);
                        }else{
                            drawerLayout.openDrawer(navigationView);
                        }
                        break;
                }
            }
        });

        reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title =  navigationView.getCheckedItem().getTitle().toString();
                nodata.setVisibility(View.INVISIBLE);
                drawerLayout.closeDrawer(navigationView);
                switchGroup.setVisibility(View.GONE);
                switchGroup_search.setVisibility(View.GONE);
                search_text.setVisibility(View.GONE);
                loddingBar.setVisibility(View.INVISIBLE);
                if (title.equals("Repository")){

                    getRepoInfo("repo");

                } else  if (title.equals("Issue")){
                    getIssueInfo();
                } else if (title.equals("Follow")){
                    RadioButton radioButton = (RadioButton)findViewById(switchGroup.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    myAdapterFollow.cleanList();
                    if (selectText.equals("Follows")) {
                        getFollowInfo("Follows");
                    } else if (selectText.equals("Following")){
                        getFollowInfo("Following");
                    }


                    getFollowInfo("Follows");
                    switchGroup.setVisibility(View.VISIBLE);

                }else  if (title.equals("Event")){
                    getEventInfo();
                } else  if (title.equals("Starred")){
                    getRepoInfo("star");
                }else  if (title.equals("Gist")){
                    getGistInfo();
                } else  if (title.equals("Setting")){
                    Intent intent =new Intent(ForeActivity.this,SettingsActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else  if (title.equals("Explore")){
                    loddingBar.setVisibility(View.INVISIBLE);
                    search_text.setVisibility(View.VISIBLE);
                    switchGroup_search.setVisibility(View.VISIBLE);
                }else  if (title.equals("About")){
                    loddingBar.setVisibility(View.INVISIBLE);
                    Intent intent =new Intent(ForeActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
            }
        });


        switchGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                nodata.setVisibility(View.INVISIBLE);
                if (!search_key_word.equals("")) {
                    loddingBar.setVisibility(View.VISIBLE);
                } else {
                    
                }

                RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                String selectText = radioButton.getText().toString();
                myAdapterFollow.cleanList();

                if (selectText.equals("Follows")) {
                    getFollowInfo("Follows");
                } else if (selectText.equals("Following")){
                    getFollowInfo("Following");
                }

            }
        });

        switchGroup_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                nodata.setVisibility(View.INVISIBLE);
                RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                String selectText = radioButton.getText().toString();
                myAdapterFollow.cleanList();
                loddingBar.setVisibility(View.VISIBLE);
                if (selectText.equals("User")) {
                    SearchRepo = false;
                    getSearchOwner(search_key_word);
                } else if (selectText.equals("Repository")){
                    SearchRepo = true;
                    getSearchRepo(search_key_word);
                }

            }
        });


        search_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法

            @Override
            public boolean onQueryTextSubmit(String query) {
                search_key_word = query;
                nodata.setVisibility(View.INVISIBLE);
                loddingBar.setVisibility(View.VISIBLE);
                if (SearchRepo) {
                    getSearchRepo(query);
                } else {
                    getSearchOwner(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }



        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
                viewName.setText(item.getTitle().toString());
                nodata.setVisibility(View.INVISIBLE);
                drawerLayout.closeDrawer(navigationView);
                myAdapterRepo.cleanList();
                myAdapterIssue.cleanList();
                myAdapterFollow.cleanList();
                switchGroup.setVisibility(View.GONE);
                switchGroup_search.setVisibility(View.GONE);
                search_text.setVisibility(View.GONE);
                loddingBar.setVisibility(View.VISIBLE);

                if (item.getTitle().toString().equals("Repository")){

                    getRepoInfo("repo");

                } else  if (item.getTitle().toString().equals("Issue")){
                    getIssueInfo();
                } else if (item.getTitle().toString().equals("Follow")){
                    RadioButton radioButton = (RadioButton)findViewById(switchGroup.getCheckedRadioButtonId());
                    String selectText = radioButton.getText().toString();
                    myAdapterFollow.cleanList();
                    if (selectText.equals("Follows")) {
                        getFollowInfo("Follows");
                    } else if (selectText.equals("Following")){
                        getFollowInfo("Following");
                    }


                    getFollowInfo("Follows");
                    switchGroup.setVisibility(View.VISIBLE);

                }else  if (item.getTitle().toString().equals("Event")){
                    getEventInfo();
                } else  if (item.getTitle().toString().equals("Starred")){
                    getRepoInfo("star");
                }else  if (item.getTitle().toString().equals("Gist")){
                    getGistInfo();
                } else  if (item.getTitle().toString().equals("Setting")){
                    loddingBar.setVisibility(View.INVISIBLE);
                    Intent intent =new Intent(ForeActivity.this,SettingsActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else  if (item.getTitle().toString().equals("Explore")){
                    loddingBar.setVisibility(View.INVISIBLE);
                    search_text.setVisibility(View.VISIBLE);
                    switchGroup_search.setVisibility(View.VISIBLE);
                }else  if (item.getTitle().toString().equals("About")){

                    loddingBar.setVisibility(View.INVISIBLE);
                    Intent intent =new Intent(ForeActivity.this,AboutActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        myAdapterFollow.setOnItemClickListener(new FollowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent =new Intent(ForeActivity.this,OwnerDetailActivity.class);
                intent.putExtra("ownerName", myAdapterFollow.getItem(position).getLogin());

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        myAdapterRepo.setOnItemClickListener(new RepoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent =new Intent(ForeActivity.this,RepoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("repo",myAdapterRepo.getItem(position) );
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        myAdapterIssue.setOnItemClickListener(new IssueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent =new Intent(ForeActivity.this,IssueDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("issue",myAdapterIssue.getItem(position) );
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });





    }

    private void initWindow() {//初始化窗口属性，让状态栏和导航栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            int statusColor = Color.parseColor("#1976d2");
            tintManager.setStatusBarTintColor(statusColor);
            tintManager.setStatusBarTintEnabled(true);
            recyclerView2 = findViewById(R.id.recyclerView2);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            myAdapterRepo =  RepoAdapter.getInstance();
            myAdapterRepo.initContext(this);
            myAdapterIssue =  IssueAdapter.getInstance();
            myAdapterIssue.initContext(this);
            myAdapterFollow = myAdapterFollow.getInstance();
            myAdapterFollow.initContext(this);
            myAdapteEvent = myAdapteEvent.getInstance();
            myAdapteEvent.initContext(this);
            myAdapteGist = myAdapteGist.getInstance();
            myAdapteGist.initContext(this);

            recyclerView2.setLayoutManager(mLayoutManager);


        }
    }

    //获取repo
    private void getRepoInfo(String flag){
        GitHubService request = retrofit.create(GitHubService.class);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterRepo);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装


        Call<ArrayList<Repo>> list_repo = null;

        if (flag.equals("repo")) {
            list_repo = request.getRepo(username);
        } else if (flag.equals("star")) {
            list_repo = request.getStarred(username);
        }


        list_repo.enqueue(new Callback<ArrayList<Repo>>() {
            @Override
            public void onResponse(Call<ArrayList<Repo>> call, Response<ArrayList<Repo>> response) {
                try {
                    ArrayList<Repo> list = response.body();

                    list_lase_issue = new ArrayList<>();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isHas_issues()){
                            list_lase_issue.add(list.get(i));
                        }
                    }

                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list_lase_issue);
                    b.putString("flag", "repo");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);


                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repo>> call, Throwable t) {

            }
        });
    }
    //获取issue
    private void getIssueInfo() {
        final GitHubService request = retrofit.create(GitHubService.class);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterIssue);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装



        Call<ArrayList<Repo>> list_repo = request.getRepo(username);

        list_repo.enqueue(new Callback<ArrayList<Repo>>() {
            @Override
            public void onResponse(Call<ArrayList<Repo>> call, Response<ArrayList<Repo>> response) {
                try {
                    //获取用户所有repo
                    ArrayList<Repo> list = response.body();

                    list_lase_issue = new ArrayList<>();

                    Log.d("fuckbug", String.valueOf(list.size()));

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isHas_issues()){
                            list_lase_issue.add(list.get(i));
                        }
                    }

                    for (int i = 0; i < list_lase_issue.size(); i++) {
                        final Repo hasIssueRepo = list_lase_issue.get(i);
                        if (hasIssueRepo.getOpen_issues() == 0) {
                            continue;
                        }

                        final Call<ArrayList<Issue>> list_repo_issue = request.getIssue(username, hasIssueRepo.getName());

                        list_repo_issue.enqueue(new Callback<ArrayList<Issue>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Issue>> call, Response<ArrayList<Issue>> response) {
                                try {
                                    ArrayList<Issue> list = response.body();

                                    Bundle b = new Bundle();
                                    b.putSerializable("msg",(Serializable) list);
                                    b.putString("flag", "issue");
                                    Message m = new Message();
                                    m.setData(b);
                                    handler.sendMessage(m);

                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                            }
                            @Override
                            public void onFailure(Call<ArrayList<Issue>> call, Throwable t) {


                            }
                        });
                    }

                    ArrayList<Issue> list2 = new ArrayList<>();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list2);
                    b.putString("flag", "issue");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);


                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repo>> call, Throwable t) {

            }
        });



    }
    //获取follow
    private void getFollowInfo(String flag){
        GitHubService request = retrofit.create(GitHubService.class);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterFollow);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装
        Call<ArrayList<Follower>> list_Follower = null ;
        if (flag.equals("Follows")) {
            list_Follower = request.getFollowers(username);

        } else if (flag.equals("Following")) {
            list_Follower = request.getFollowering(username);
        }


        list_Follower.enqueue(new Callback<ArrayList<Follower>>() {
            @Override
            public void onResponse(Call<ArrayList<Follower>> call, Response<ArrayList<Follower>> response) {
                try {
                    ArrayList<Follower> list = response.body();

                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list);
                    b.putString("flag", "follow");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);


                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Follower>> call, Throwable t) {

            }
        });
    }
    //获取event
    private void getEventInfo(){
        GitHubService request = retrofit.create(GitHubService.class);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteEvent);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装
        Call<ArrayList<Event>> list_Event = request.getEvents(username);

        list_Event.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                try {
                    ArrayList<Event> list = response.body();

                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) list);
                    b.putString("flag", "event");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);


                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

            }
        });
    }
    //获取gist
    private void getGistInfo(){
        GitHubService request = retrofit.create(GitHubService.class);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapteGist);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));
        //对 发送请求 进行封装
        Call<ArrayList<Gist>> list_Event = request.getGists(username);

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
                    Toast.makeText(ForeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Gist>> call, Throwable t) {

            }
        });
    }
    //获取owner
    private void getOwnerInfo() {
        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装
        Call<Owner> list_Event = request.getBaseUserInfo(username);

        list_Event.enqueue(new Callback<Owner>() {
            @Override
            public void onResponse(Call<Owner> call, Response<Owner> response) {
                try {
                    Owner owner = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) owner);
                    b.putString("flag", "owner");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<Owner> call, Throwable t) {

            }
        });
    }

    private void getSearchRepo(String key_word) {
        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterRepo);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));

        Call<RepoSearchReturn> list_Event = request.getSearchRepo(key_word);

        list_Event.enqueue(new Callback<RepoSearchReturn>() {
            @Override
            public void onResponse(Call<RepoSearchReturn> call, Response<RepoSearchReturn> response) {
                try {
                    RepoSearchReturn returns = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) returns.getRepoList());
                    b.putString("flag", "repo");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<RepoSearchReturn> call, Throwable t) {

            }
        });
    }

    private void getSearchOwner(String key_word) {
        GitHubService request = retrofit.create(GitHubService.class);
        //对 发送请求 进行封装
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapterFollow);
        scaleInAnimationAdapter.setDuration(500);
        recyclerView2.setAdapter((scaleInAnimationAdapter));


        Call<OwnerSearchReturn> list_Event = request.getSearchusers(key_word);

        list_Event.enqueue(new Callback<OwnerSearchReturn>() {
            @Override
            public void onResponse(Call<OwnerSearchReturn> call, Response<OwnerSearchReturn> response) {
                try {
                    OwnerSearchReturn returns = response.body();
                    Bundle b = new Bundle();
                    b.putSerializable("msg",(Serializable) returns.getOwnerList());
                    b.putString("flag", "follow");
                    Message m = new Message();
                    m.setData(b);
                    handler.sendMessage(m);

                } catch (Exception e) {
                    Toast.makeText(ForeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    Log.d("error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<OwnerSearchReturn> call, Throwable t) {

            }
        });
    }



}
