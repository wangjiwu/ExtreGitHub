/**
 * Copyright 2018 bejson.com
 */
package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auto-generated: 2018-12-07 13:17:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class Comment implements Serializable {

    @SerializedName("body")
    private String body;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("user")
    private Owner user;

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Owner getUser() {
        return user;
    }
}
