/**
 * Copyright 2018 bejson.com
 */
package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Auto-generated: 2018-12-07 13:17:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class Follower implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatar_url;

    Follower(String login, String avatar_url, String id) {
        this.login = login;
        this.avatar_url = avatar_url;
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}