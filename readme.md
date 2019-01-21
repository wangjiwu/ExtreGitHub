
# 应用类图

先用一些表格表示各个文件的作用
## 界面Activity类：
|界面Activity| 作用  |
|--|--|
| AboutActivity |  应用关于界面 |
| ForeActivity|  应用主界面 |
| IssueDetailActivity|  Issue详情界面 |
| LoginActivity|  登陆界面 |
| OwnerDetailActivity|  用户详情界面 |
| RepoDetailActivity|  仓库详情界面 |
| SettingsActivity|  设置界面 |
|RepoEventActivity|Repo的Event 和 用户的Gist和Event界面  **Gist和Event可以区分是因为设置了不同的Adapter** |
|RepoForksActivity | Repo的Fork仓库显示， 用户的Repo， Starred仓库显示， **因为都是显示仓库， 只是API不同，所以只用一个java类**|
|RepoIssueActivity | Repo的Issue显示  |
|RepoStargazerActivity| Repo的Stargazers，Contributors， 用户的Followers， Following， **访问的API不同，可以显示不同内容**   |
## 列表Adapter：

|Adapter名| 作用 |
|--|--|
|CommentAdapter|  用于适配显示issue评论 |
|EventAdapter|  用于适配显示Event |
|FollowAdapter|  用于适配显示Followers和Following评论 |
|GistAdapter|  用于适配显示Gist |
|IssueAdapter|  用于适配显示issue |
|RepoAdapter|  用于适配显示repo |

## 基础实体类：
基础类四用于把api获得的json解析成类对象， 便于更好的处理

|类名| 作用 |
|--|--|
|  AccessToken|  表示api返回的token类， 里面还有token属性 |
|Comment  | 用于表示issue的comment 对象|
|Event| 用于表示Event 对象|
|Follower| 用于表示Follower对象 （包括follow和被follow） |
|Gist| 用于表示Gist 对象|
|Issue| 用于表示issue对象|
|Owner| 用于表示Owner 也就是用户|
|Repo| 用于表示Repo对象|
|RepoSearchReturn| 用于表示Search的Repo结果 ， 包含一个 Repo的列表和列表长度|
|OwnerSearchReturn| 用于表示Search的Owner结果 ， 包含一个 Owner的列表和列表长度|
## 其他类
|类名| 作用 |
|--|--|
|GitHubService| 接口类 用于api访问
|XCRoundImageView| 继承Imageview类实现 圆形图片|

## 界面跳转逻辑

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121005501591.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)

# 所采用的技术和采用该技术的原因
## 1. 登陆功能实现

github登陆功能我们使用的是更为**安全的Oauth2 验证**， 验证的过程是， 首先我将传入我在github申请的**两个密钥**， 来访问验证网页， 用户**通过在打开的github验证网页里输入用户名和密码给我们授权**， 然后通过我设置的回调网页**返回用户的token**， 此时token就代表着用户的权限， 我可以通过token对API进行大量的访问，以及其他更多的权限

具体博客可以看https://blog.csdn.net/huyaoyu/article/details/79434110


那么如何判断用户是否已经验证成功呢？

我们的做法是 是把当前用户的用户名存起来表示**上一次登陆的用户名**，key为**loginIn**， 并且把**此用户的token也记下来**， 如果再一次打开应用， 首先查看 loginIn是否为空， **如果不为空表示不用重新登陆。否则需要重新访问网页进行授权**，那么注销时就 只要把的loginIn变为none（github没有用户名没none的， 可能是便于我们开发吧）同时为了安全 需要删除token即可。

```java
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
```

## 2. 显示内容

显示是根据上次Github的实验， 上次只是做了Repo， 所以需要扩展接口， 以及其他的实体类， Adapter类。 一共扩展了
了**10个实体类**， 通过Handle 实现UI更新



```java
if (msg.getData().getString("flag").equals("repo")) {
    ArrayList<Repo> list =  (ArrayList<Repo>)msg.getData().getSerializable("msg");


    if (list == null) {
        Toast.makeText(ForeActivity.this, "此用户没有任何仓库", Toast.LENGTH_SHORT).show();
    } else {
        try {

            myAdapter.addObjects(list);

        } catch (Exception e) {
            Toast.makeText(ForeActivity.this, "数据库中不存在记录", Toast.LENGTH_SHORT).show();
        }
    }
} 
```
在ForeActivity里， 实际上需要根据按不同的按钮来显示不同的list内容， 我的处理方法是， **通过判断点击的按钮的Lable作出不同的处理**（调用相应的API， 并且设置相应的Adapter， **向Handle发送不同的Falg来防止误加操作**），通过在**同一个RecycleView设置不同的Adapter**，实现显示不同的内容 




## 3. 侧滑窗
为了优化UI我们使用最常见的侧滑窗实现，
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121013300141.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)

侧滑窗通过
**DrawerLayout  +  NavigationView 实现**
侧滑窗显示和去除 实现如下
```java
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
```

## 4. 分享功能

分享功能是通过系统启动不同应用来**发送一句话， 代码很简单，但是效果很好**

```java
Intent intent=new Intent(Intent.ACTION_SEND);
intent.setType("image/*");
intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
intent.putExtra(Intent.EXTRA_TEXT, "我正在用ExtreGithub 这个APP， 要来看看嘛٩(๑❛ᴗ❛๑)۶ , github地址如下： https://github.com/wangjiwu/ExtreGithub" );
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(Intent.createChooser(intent, getTitle()));

```

## 5. 如何添加token
因为前面的实验token是写死的， 我们显然是不能这样做的， 最好的做法是**设置拦截器**， 通过拦截从**okhttpClient发出的请求**， 然后在请求的header上加上用户的token就可以保证访问成功了

```java
//设置头的token
OkHttpClient build = new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() { //加入拦截器 
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



```
## 6.如何加载网络图片

以前的做法是自己写一个类继承Imageview 然后设置它的url就可以访问， 这样的方法不好的一点是，他没有图片缓存， 访问同一个url的图片需要重新发送请求， 我们做法是用别人的轮子——**Picasso图片访问框架， 自带图片缓存，而且上手简单。**

```java
 Picasso.with(ForeActivity.this).load(owner.getAvatar_url())
        						.noFade()
          						.into( personImage);
```


## 7.界面优化

界面优化感觉提升比较大的大的改动就是

### 7.1 进度条显示
因为要进行网络连接有一定的延迟，所以进度条可以极大的增加用户体验。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121015103976.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)

实现方式

 - 首先在界面中央放置一个进度条
 - 在handle结束时， 把进度条设置为不可见
 - 在进行网络访问是把进度条设置为可见

### 7.2 no data 显示
**在handle里面得到Api返回解析的List， 判断List的大小为0则把 No Data设置为可见， 每次进行访问都先设置为不可见**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121015220421.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)

### 7.3 自适应的列表项

因为有些带内容的文字，高度不定， 所以我们需要**设置宽度为Warp-Content**这样效果会好很多， 而且设置**MinHeight** 保证当没有内容时，列表项难看

最终实现效果是这样的
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121015810560.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)
还是比较美观的

### 7.4 头像栏 阴影化

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190121020538872.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxODc0NDU1OTUz,size_16,color_FFFFFF,t_70)
这一段因为是直接给layout设置背景图片 **不能直接用with into 这样简单的设置**， 必须先通过网络访问得到一个target 然后设置图片为target才可以， **得到图片的drawable时， 需要设置它变灰才达到理想效果**。

 **drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);**


```java
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
```



