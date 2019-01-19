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

    //@Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}")  //获取用户详情
    Call<Owner> getBaseUserInfo(@Path("user_name") String user_name);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/repos")  //获取用户仓库详情
    Call<ArrayList<Repo>> getRepo(@Path("user_name") String user_name);

    //  @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/followers") // 获取用户的followers
    Call<ArrayList<Follower>> getFollowers(@Path("user_name") String user_name);

    //  @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/following")// 获取用户的followering
    Call<ArrayList<Follower>> getFollowering(@Path("user_name") String user_name);

    //  @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/events") // 获取用户的event
    Call<ArrayList<Event>> getEvents(@Path("user_name") String user_name);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/starred")// 获取用户的star的项目仓库详情
    Call<ArrayList<Repo>> getStarred(@Path("user_name") String user_name);

    //  @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/search/repositories")// 获取用户的star的项目仓库详情
    Call<RepoSearchReturn> getSearchRepo(@Query("q") String key_word);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/search/users")// 获取用户的star的项目仓库详情
    Call<OwnerSearchReturn> getSearchusers(@Query("q") String key_word);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/users/{user_name}/gists")// 获取用户的gists详情
    Call<ArrayList<Gist>> getGists(@Path("user_name") String user_name);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/issues")// 获取仓库的issue详情
    Call<ArrayList<Issue>> getIssue(@Path("user_name") String user_name, @Path("repo") String repo);

    //  @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/events")// 获取仓库的issue详情
    Call<ArrayList<Event>> getRepoEvent(@Path("user_name") String user_name, @Path("repo") String repo);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/issues")// 获取仓库的issue详情
    Call<ArrayList<Issue>> getRepoIssue(@Path("user_name") String user_name, @Path("repo") String repo);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/stargazers")// 获取仓库的issue详情
    Call<ArrayList<Follower>> getRepoStargazers(@Path("user_name") String user_name, @Path("repo") String repo);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/contributors")// 获取仓库的issue详情
    Call<ArrayList<Follower>> getRepoContributors(@Path("user_name") String user_name, @Path("repo") String repo);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("/repos/{user_name}/{repo}/forks")// 获取仓库的issue详情
    Call<ArrayList<Repo>> getRepoForks(@Path("user_name") String user_name, @Path("repo") String repo);

    // @Headers("Authorization: token d95c37ed9a160b4867e8390a9b4c621595521633")
    @GET("comments")// 获取仓库的issue详情
    Call<ArrayList<Comment>> getIssueComments();

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

   @GET("/user")// 获取用户的star的项目仓库详情
   Call<Follower> getLoginInfo(@Query("access_token") String key_word);

    @POST("/repos/{user_name}/{repo}/forks")// 获取用户的star的项目仓库详情
    Call<Follower> postForkRequest (@Path("user_name") String user_name, @Path("repo") String repo);




}