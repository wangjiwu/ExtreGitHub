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

public class Gist implements Serializable {

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("comments")
    private String comments;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("comments_url")
    private String comments_url;


    Gist(String updated_at, String created_at,String comments,
         String description, String comments_url, Owner owner){
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.comments = comments;
        this.description = description;
        this.comments_url = comments_url;
        this.owner = owner;


    }

    public Owner getOwner() {
        return owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public String getComments() {
        return comments;
    }

    public String getComments_url() {
        return comments_url;
    }

    public String getUpdated_at() {
        return updated_at;
    }

}
