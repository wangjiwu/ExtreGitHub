package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Owner  implements Serializable {

    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatar_url;

    @SerializedName("blog")
    private String blog;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private String location;

    @SerializedName("company")
    private String company;


    Owner (String login,String avatar_url,String blog,String created_at,String email, String name, String location, String company){
        this.location = location;
        this.avatar_url = avatar_url;
        this.blog = blog;
        this.company = company;
        this.email = email;
        this.created_at = created_at;
        this.login = login;
        this.name = name;

    }

    public String getLogin() {
        return login;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getBlog() {
        return blog;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

}
