package com.example.lenovo.httpapi;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("/users/{user_name}")  //获取用户详情
    Call<Owner> getBaseUserInfo(@Path("user_name") String user_name);

    @GET("/users/{user_name}/repos")  //获取用户仓库详情
    Call<ArrayList<Repo>> getRepo(@Path("user_name") String user_name);

    @GET("/users/{user_name}/followers") // 获取用户的followers
    Call<ArrayList<Follower>> getFollowers(@Path("user_name") String user_name);

    @GET("/users/{user_name}/following")// 获取用户的followering
    Call<ArrayList<Follower>> getFollowering(@Path("user_name") String user_name);

     @GET("/users/{user_name}/events") // 获取用户的event
    Call<ArrayList<Event>> getEvents(@Path("user_name") String user_name);

    @GET("/users/{user_name}/starred")// 获取用户的star的项目仓库详情
    Call<ArrayList<Repo>> getStarred(@Path("user_name") String user_name);

    @GET("/search/repositories")// 获取用户的star的项目仓库详情
    Call<RepoSearchReturn> getSearchRepo(@Query("q") String key_word);

    @GET("/search/users")// 获取用户的star的项目仓库详情
    Call<OwnerSearchReturn> getSearchusers(@Query("q") String key_word);

     @GET("/users/{user_name}/gists")// 获取用户的gists详情
    Call<ArrayList<Gist>> getGists(@Path("user_name") String user_name);

     @GET("/repos/{user_name}/{repo}/issues")// 获取ssue详情
    Call<ArrayList<Issue>> getIssue(@Path("user_name") String user_name, @Path("repo") String repo);

    @GET("/repos/{user_name}/{repo}/events")// 获取仓库的events详情
    Call<ArrayList<Event>> getRepoEvent(@Path("user_name") String user_name, @Path("repo") String repo);

    @GET("/repos/{user_name}/{repo}/issues")// 获取仓库的issue详情
    Call<ArrayList<Issue>> getRepoIssue(@Path("user_name") String user_name, @Path("repo") String repo);

    @GET("/repos/{user_name}/{repo}/stargazers")// 获取仓库的stargazers详情
    Call<ArrayList<Follower>> getRepoStargazers(@Path("user_name") String user_name, @Path("repo") String repo);

    @GET("/repos/{user_name}/{repo}/contributors")// 获取仓库的contributors详情
    Call<ArrayList<Follower>> getRepoContributors(@Path("user_name") String user_name, @Path("repo") String repo);

    @GET("/repos/{user_name}/{repo}/forks")// 获取仓库的forks详情
    Call<ArrayList<Repo>> getRepoForks(@Path("user_name") String user_name, @Path("repo") String repo);

    //这里的baseURl是变化了
    @GET("comments")// 获取仓库的comments详情
    Call<ArrayList<Comment>> getIssueComments();

    //获取code 进行授权登录
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    // 通过accesstoken获取 此token代表的用户
   @GET("/user")// 获取用户的star的项目仓库详情
   Call<Follower> getLoginInfo(@Query("access_token") String key_word);


}