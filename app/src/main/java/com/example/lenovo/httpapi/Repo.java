/**
 * Copyright 2018 bejson.com
 */
package com.example.lenovo.httpapi;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto-generated: 2018-12-07 13:17:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class Repo implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("open_issues_count")
    private int open_issues_count;
    @SerializedName("size")
    private int size;

    @SerializedName("html_url")
    private String html_url;

    @SerializedName("has_issues")
    private boolean has_issues;
    @SerializedName("language")
    private String language;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("forks_count")
    private String forks_count;
    @SerializedName("watchers")
    private String watchers;
    @SerializedName("stargazers_count")
    private String stargazers_count;


    @SerializedName("owner")
    private Owner owner;



    Repo( String name,String description,String id,int open_issues,
          boolean has_issues, Owner owner, String language,
          String forks_count, String watchers, String stargazers_count){
        this.name = name;
        this.description = description;
        this.id = id;
        this.open_issues_count = open_issues;
        this.has_issues = has_issues;
        this.owner = owner;
        this.language = language;
        this.forks_count = forks_count;
        this.watchers = watchers;
        this.stargazers_count = stargazers_count;
    }

    public String getId() {
        return id;
    }

    public int getOpen_issues() {
        return open_issues_count;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean isHas_issues() {
        return has_issues;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getForks_count() {
        return forks_count;
    }

    public String getLanguage() {
        return language;
    }

    public String getStargazers_count() {
        return stargazers_count;
    }

    public String getWatchers() {
        return watchers;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getSize() {
        return size;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getHtml_url() {
        return html_url;
    }
}