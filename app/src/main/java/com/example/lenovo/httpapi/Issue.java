package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Issue implements Serializable {
    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("state")
    private String state;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("body")
    private String body;
    @SerializedName("comments")
    private String comments;
    @SerializedName("user")
    private Owner user;
    @SerializedName("number")
    private String number;

    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public String getCreated_at() {
        return created_at;
    }
    public String getState() {
        return state;
    }

    public String getComments() {
        return comments;
    }

    public String getNumber() {
        return number;
    }

    public Owner getUser() {
        return user;
    }

    public String getUrl() {
        return url;
    }
}