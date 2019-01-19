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

public class RepoSearchReturn implements Serializable {

    @SerializedName("total_count")
    private Integer total_count;

    @SerializedName("items")
    private ArrayList<Repo> RepoList;



    public ArrayList<Repo> getRepoList() {
        return RepoList;
    }

    public Integer getTotal_count() {
        return total_count;
    }
}
